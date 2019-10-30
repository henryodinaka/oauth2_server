package market.henry.auth.services;

import lombok.extern.slf4j.Slf4j;
import market.henry.auth.config.email.SendEmailNotifications;
import market.henry.auth.dto.AccountCheckRequest;
import market.henry.auth.dto.BvnDetails;
import market.henry.auth.enums.ResponseCode;
import market.henry.auth.exceptions.AuthServerException;
import market.henry.auth.model.User;
import market.henry.auth.repo.UserRepo;
import market.henry.auth.services.redis.RedisService;
import market.henry.auth.utils.Response;
import market.henry.auth.utils.Validation;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class CommonService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RedisService redisService;
    @Autowired
    private Environment environment;
    @Autowired
    private SendEmailNotifications sendEmailNotifications;

    public ResponseEntity validateBvn(String bvn){
        User user = userRepo.getByBvn(bvn);
        if (user == null)
        {
            log.info("Invalid bvn; User not found");
            return ResponseEntity.ok(new Response(404,"No user is tied to this bvn",null));
        }
        try {
            return Response.setUpResponse(202,"BVN valid","",new BvnDetails(user));
        } catch (AuthServerException e) {
            log.error("ERROR::::::",e);
            return Response.setUpResponse(e.getHttpCode(),e.getMessage(),"",null);
        }
    }
    public ResponseEntity checkAccountExist(AccountCheckRequest accountCheckRequest){
        String error = Validation.validateAccountCheckRequest(accountCheckRequest);
        if (error !=null) return Response.setUpResponse(400,error);
        User user = userRepo.checkAccountExist(accountCheckRequest.getFirstName(),accountCheckRequest.getLastName());
        if (user == null)
        {
            log.info("Invalid bvn; User not found");
            return ResponseEntity.ok(new Response(404,"User has no account",null));
        }
        try {
            return Response.setUpResponse(202,"User has account","",new BvnDetails(user));
        } catch (AuthServerException e) {
            log.error("ERROR::::::",e);
            return Response.setUpResponse(e.getHttpCode(),e.getMessage(),"",null);
        }
    }
    public ResponseEntity accountInquiry(String accountNumber){

        if (!Validation.validNumberLength(accountNumber,10)) return Response.setUpResponse(400,"Account number must be 10 digits");
        User user = userRepo.accountInquiry(accountNumber);
        if (user == null)
        {
            log.info("Invalid Account number; User not found");
            return ResponseEntity.ok(new Response(404,"This account number does not exist on our systems",null));
        }
        try {
            return Response.setUpResponse(202,"User has account","",new BvnDetails(user));
        } catch (AuthServerException e) {
            log.error("ERROR::::::",e);
            return Response.setUpResponse(e.getHttpCode(),e.getMessage(),"",null);
        }
    }
    public ResponseEntity accountBalanceInquiry(String accountNumber){

        if (!Validation.validNumberLength(accountNumber,10)) return Response.setUpResponse(400,"Account number must be 10 digits");
        BigDecimal balanceInquiry = userRepo.accountBalanceInquiry(accountNumber);
        if (balanceInquiry == null)
        {
            log.info("Invalid Account number; User not found");
            return ResponseEntity.ok(new Response(404,"This account number does not exist on our systems",null));
        }
        return Response.setUpResponse(202,"User has account","",balanceInquiry);
    }
    public ResponseEntity generateSecret(String phoneNumber,String channelCode) throws AuthServerException {

        String secret = null;
        String message = null;
        User user = userRepo.getByPhoneNumber(phoneNumber);
        if (user == null) return Response.setUpResponse(ResponseCode.ITEM_NOT_FOUND,"Phone number",null);
        switch (channelCode){
            case "01": secret =  RandomStringUtils.random(6,false,true);
                message = "Token";
            break;
            case "02":
            case "03": secret =  RandomStringUtils.random(4,false,true);
                message = "OTP";
            break;
            default:throw new AuthServerException(400,"Invalid channel code");
        }
        String tokenTimeOut = environment.getProperty("app.opt.timeout");

        boolean saved = redisService.saveRecordToRedis(secret, phoneNumber, Long.parseLong(tokenTimeOut == null ? "15" : tokenTimeOut));
        if (!saved)
            return Response.setUpResponse(ResponseCode.UNAVAILABLE,message,null);
        sendEmail(user.getEmail(),secret,tokenTimeOut);
        return Response.setUpResponse(ResponseCode.SUCCESS,"",secret);
    }
    public ResponseEntity validateSecret(String phoneNumber,String secret,String channelCode) throws AuthServerException {

        User user = userRepo.getByPhoneNumber(phoneNumber);
        if (user == null) return Response.setUpResponse(ResponseCode.ITEM_NOT_FOUND,"Phone number",null);
        log.info("Incoming phoneNumber {} Secret {} User phone number {} secret {}",phoneNumber,secret,user.getPhoneNumber(),user.getPin()+" token "+user.getToken());
        boolean isValid = false;
        String message = null;
        System.out.println("The code "+channelCode);

        switch (channelCode){
            case "01":
                message = "Token";
                if (user.getToken().equals(Integer.parseInt(secret)))
                    isValid = true;
            break;
            case "02":
            case "03":
                message = "Pin";
                if (user.getPin().equals(Integer.parseInt(secret)))
                    isValid = true;
            break;
            default:throw new AuthServerException(400,"Invalid channel code");
        }
        if (!isValid) {
            String secretSaved = redisService.getRecordFromRedis(phoneNumber, String.class);

            if (secretSaved == null)
                return ResponseEntity.ok(new Response(408, "Invalid " + message + " ", null));

            if (!secretSaved.equalsIgnoreCase(secret))
                return ResponseEntity.ok(new Response(400, message + " is invalid", null));
        }
        return Response.setUpResponse(202,message+" is valid","",null);
    }

    @Async
    public void sendEmail(String email,String otp,String expires){
        log.info("Sending otp to bvn email {} OTP {}",email,otp);
        sendEmailNotifications.sendApprovalMail(email,otp,expires);
    }
}
