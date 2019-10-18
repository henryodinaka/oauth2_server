package market.henry.auth.services;

import lombok.extern.slf4j.Slf4j;
import market.henry.auth.config.email.SendEmailNotifications;
import market.henry.auth.dto.BvnDetails;
import market.henry.auth.enums.ResponseCode;
import market.henry.auth.exceptions.AuthServerException;
import market.henry.auth.model.User;
import market.henry.auth.repo.UserRepo;
import market.henry.auth.services.redis.RedisService;
import market.henry.auth.utils.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    public ResponseEntity generateSecret(String phoneNumber,String channelCode) throws AuthServerException {

        String secret = null;
        String message = null;

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
        sendEmail(phoneNumber,secret,tokenTimeOut);
        return Response.setUpResponse(ResponseCode.SUCCESS,"",secret);
    }
    public ResponseEntity validateSecret(String phoneNumber,String secret,String channelCode) throws AuthServerException {

        String message = null;
        System.out.println("The code "+channelCode);

        switch (channelCode){
            case "01":
                message = "Token";
            break;
            case "02":
            case "03":
                message = "OTP";
            break;
            default:throw new AuthServerException(400,"Invalid channel code");
        }

        String secretSaved = redisService.getRecordFromRedis(phoneNumber, String.class);

        if (secretSaved==null)
            return ResponseEntity.ok(new Response(408,message+" has expired",null));

        if (!secretSaved.equalsIgnoreCase(secret))
            return ResponseEntity.ok(new Response(400,message+" is invalid",null));

        return Response.setUpResponse(202,message+" is valid","",null);
    }

    @Async
    public void sendEmail(String email,String otp,String expires){
        log.info("Sending otp to bvn email {} OTP {}",email,otp);
        sendEmailNotifications.sendApprovalMail(email,otp,expires);
    }
}
