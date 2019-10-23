  package market.henry.auth.controller;

import lombok.extern.slf4j.Slf4j;
import market.henry.auth.dto.AccountCheckRequest;
import market.henry.auth.dto.AccountCreationRequest;
import market.henry.auth.dto.SecretRequest;
import market.henry.auth.enums.ResponseCode;
import market.henry.auth.exceptions.AuthServerException;
import market.henry.auth.services.AccountService;
import market.henry.auth.services.AuthorizationService;
import market.henry.auth.services.CommonService;
import market.henry.auth.utils.Response;
import market.henry.auth.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

  @RestController
  @Slf4j
public class CommonController {

    private CommonService commonService;
    private AuthorizationService authorizationService;
    private AccountService accountService;

    @Autowired
      public CommonController(CommonService commonService, AuthorizationService authorizationService, AccountService accountService) {
          this.commonService = commonService;
          this.authorizationService = authorizationService;
          this.accountService = accountService;
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