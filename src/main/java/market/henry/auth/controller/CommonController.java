  package market.henry.auth.controller;

import lombok.extern.slf4j.Slf4j;
import market.henry.auth.dto.AccountCheckRequest;
import market.henry.auth.dto.AccountCreationRequest;
import market.henry.auth.dto.LoanRequest;
import market.henry.auth.dto.SecretRequest;
import market.henry.auth.enums.ResponseCode;
import market.henry.auth.exceptions.AuthServerException;
import market.henry.auth.model.StartWorkFlow;
import market.henry.auth.services.AccountService;
import market.henry.auth.services.AuthorizationService;
import market.henry.auth.services.CommonService;
import market.henry.auth.services.StarWorkService;
import market.henry.auth.utils.Response;
import market.henry.auth.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

  @RestController
  @Slf4j
public class CommonController {

    private CommonService commonService;
    private AuthorizationService authorizationService;
    private AccountService accountService;
    private StarWorkService starWorkService;

    @Autowired
      public CommonController(CommonService commonService, AuthorizationService authorizationService, AccountService accountService,StarWorkService starWorkService) {
          this.commonService = commonService;
          this.authorizationService = authorizationService;
          this.accountService = accountService;
          this.starWorkService=starWorkService;
      }

      @GetMapping("/validate/bvn")
  public ResponseEntity bvnValidation(@RequestParam String bvn, HttpServletRequest httpServletRequest) {
      Response response = authorizationService.validateInternalCall(httpServletRequest);
      if (!"00".equalsIgnoreCase(response.getStatusCode()))return ResponseEntity.ok(response);

      return commonService.validateBvn(bvn);
  }

  @PostMapping("/payment")
  public ResponseEntity makePayment(@RequestBody AccountCheckRequest accountCheckRequest, HttpServletRequest httpServletRequest) {
      return Response.setUpResponse(202,"Payment was successful");
  }

  @PostMapping("/avr")
  public ResponseEntity avr(HttpServletRequest httpServletRequest) {
      return Response.setUpResponse(202,"AVR initiated successfully");
  }

  @PostMapping("/account/check")
  public ResponseEntity checkAccountExist(@RequestBody AccountCheckRequest accountCheckRequest, HttpServletRequest httpServletRequest) {
      Response response = authorizationService.validateInternalCall(httpServletRequest);
      if (!"00".equalsIgnoreCase(response.getStatusCode()))return ResponseEntity.ok(response);

      return commonService.checkAccountExist(accountCheckRequest);
  }

  @GetMapping("/account/inquiry")
  public ResponseEntity accountInquiry(@RequestParam String accountNumber, HttpServletRequest httpServletRequest) {
      Response response = authorizationService.validateInternalCall(httpServletRequest);
      if (!"00".equalsIgnoreCase(response.getStatusCode()))return ResponseEntity.ok(response);

      return commonService.accountInquiry(accountNumber);
  }

  @PostMapping("/star/status")
  public ResponseEntity getStarStatus(@RequestBody List<String> applicationNumbers, HttpServletRequest httpServletRequest) {
        log.info("Incomming request for start status {}",applicationNumbers);
      Response response = authorizationService.validateInternalCall(httpServletRequest);
      if (!"00".equalsIgnoreCase(response.getStatusCode()))return ResponseEntity.ok(response);

      List<StartWorkFlow> stars = starWorkService.getStarStatus(applicationNumbers);
      log.info("Stars retrieved {}",stars);
      return Response.setUpResponse(ResponseCode.SUCCESS,"",stars);
  }

  @PostMapping("/disburse/status")
  public ResponseEntity getStarDisbursementStatus(@RequestBody List<String> applicationNumbers, HttpServletRequest httpServletRequest) {
        log.info("Incomming request for start status {}",applicationNumbers);
      Response response = authorizationService.validateInternalCall(httpServletRequest);
      if (!"00".equalsIgnoreCase(response.getStatusCode()))return ResponseEntity.ok(response);

      List<StartWorkFlow> stars = starWorkService.upadteAndDisburse(applicationNumbers);
      log.info("Stars retrieved {}",stars);
      return Response.setUpResponse(ResponseCode.SUCCESS,"",stars);
  }

  @GetMapping("/account/balance/inquiry")
  public ResponseEntity accountBalanceInquiry(@RequestParam String accountNumber, HttpServletRequest httpServletRequest) {
      Response response = authorizationService.validateInternalCall(httpServletRequest);
      if (!"00".equalsIgnoreCase(response.getStatusCode()))return ResponseEntity.ok(response);

      return commonService.accountBalanceInquiry(accountNumber);
  }

  @PostMapping("/create/account")
  public ResponseEntity createAccount(@RequestBody AccountCreationRequest accountCheckRequest, HttpServletRequest httpServletRequest) {
      Response response = authorizationService.validateInternalCall(httpServletRequest);
      if (!"00".equalsIgnoreCase(response.getStatusCode()))return ResponseEntity.ok(response);

      try {
          return accountService.setup(accountCheckRequest);
      } catch (AuthServerException e) {
          log.error("Error ",e);
          return Response.setUpResponse(e.getHttpCode(),e.getMessage());
      }
  }

  @PostMapping("/reactivate/account")
  public ResponseEntity reactivateAccount(@RequestBody AccountCreationRequest accountCheckRequest, HttpServletRequest httpServletRequest) {
      Response response = authorizationService.validateInternalCall(httpServletRequest);
      if (!"00".equalsIgnoreCase(response.getStatusCode()))return ResponseEntity.ok(response);

      try {
          return accountService.setup(accountCheckRequest);
      } catch (AuthServerException e) {
          log.error("Error ",e);
          return Response.setUpResponse(e.getHttpCode(),e.getMessage());
      }
  }
  @PostMapping("/starwork")
  public ResponseEntity queueToStarWorkFlow(@RequestBody LoanRequest loanRequest, HttpServletRequest httpServletRequest) {
      Response response = authorizationService.validateInternalCall(httpServletRequest);
      if (!"00".equalsIgnoreCase(response.getStatusCode()))return ResponseEntity.ok(response);

      return starWorkService.setup(loanRequest);
  }
  @GetMapping("/generate/secret")
  public ResponseEntity generateSecret(@RequestParam String phoneNumber, HttpServletRequest httpServletRequest) {
      ResponseEntity responseEntity = null;
     try {
         Response response = authorizationService.validateInternalCall(httpServletRequest);
         if (!"00".equalsIgnoreCase(response.getStatusCode())) return ResponseEntity.ok(response);
         String channelCode = httpServletRequest.getHeader("ChannelCode");
         if (!Validation.validData(channelCode)) {
             responseEntity = Response.setUpResponse(200, "Invalid channel code", "", null);
             log.info("{}",responseEntity);
             return responseEntity;
         }
           responseEntity = commonService.generateSecret(phoneNumber, channelCode);
         log.info("{}",responseEntity);
         return responseEntity;
     }catch (AuthServerException e){
          responseEntity = Response.setUpResponse(e.getHttpCode(), e.getMessage(), "", null);
          log.info("{}",responseEntity);
         return responseEntity;
     }

  }

  @PostMapping("/validate/secret")
  public ResponseEntity validateSecret(@RequestBody SecretRequest request, HttpServletRequest httpServletRequest) {
     try {
         Response response = authorizationService.validateInternalCall(httpServletRequest);
         if (!"00".equalsIgnoreCase(response.getStatusCode())) return ResponseEntity.ok(response);
         String channelCode = httpServletRequest.getHeader("ChannelCode");
         String error = Validation.validateSecretRequest(request, channelCode);
         if (error != null) {
             ResponseEntity<Response> res = ResponseEntity.ok(Response.setUpResponse(ResponseCode.BAD_REQUEST, error));
             log.info("{}",res);
             return res;
         }
         ResponseEntity responseEntity = commonService.validateSecret(request.getPhoneNumber(), request.getSecret(), channelCode);
         log.info("{}",responseEntity);
         return responseEntity;
     }catch (AuthServerException e){
         return Response.setUpResponse(e.getHttpCode(),e.getMessage(),"",null);
     }
  }

  @RolesAllowed({"ROLE_ADMIN"})
  @GetMapping("/admin")
  public String welcomeAdmin(HttpServletRequest httpServletRequest)
  {
      String channel = httpServletRequest.getHeader("channel");
      String authorization = httpServletRequest.getHeader("Authorization");

      return "welcome admin";
  }

  @RolesAllowed({"ROLE_USER"})
  @GetMapping("/user")
  public String welcomeUser() {
    return "welcome user";
  }
}