package market.henry.auth.controller;

import lombok.extern.slf4j.Slf4j;
import market.henry.auth.exceptions.AuthServerException;
import market.henry.auth.services.AuthorizationService;
import market.henry.auth.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/oauth/token")
@Slf4j
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;
    @GetMapping("/generate")
    public ResponseEntity generateToken(HttpServletRequest httpServletRequest){
        String clientId = httpServletRequest.getHeader("ClientId");
        String publicKey = httpServletRequest.getHeader("PublicKey");
//        String ChannelCode = httpServletRequest.getHeader("ChannelCode");

        String token = null;
        try {
            token = authorizationService.generateToken(clientId,publicKey);
        } catch (AuthServerException e) {
            e.printStackTrace();
            return ResponseEntity.ok(new Response(e.getHttpCode(),e.getMessage(),null));
        }
        return ResponseEntity.ok(new Response(202,"Token was generated",token));
    }
    @GetMapping("/validate")
    public ResponseEntity validateToken(HttpServletRequest httpServletRequest){
        Response response = authorizationService.validateInternalCall(httpServletRequest);
        if ("00".equalsIgnoreCase(response.getStatusCode()))
            return ResponseEntity.ok(response);
        return ResponseEntity.ok(response);
    }
}
