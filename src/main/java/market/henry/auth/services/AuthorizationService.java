package market.henry.auth.services;

import lombok.extern.slf4j.Slf4j;
import market.henry.auth.exceptions.AuthServerException;
import market.henry.auth.services.redis.RedisService;
import market.henry.auth.utils.Response;
import market.henry.auth.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
@Slf4j
public class AuthorizationService {
    @Autowired
    RedisService redisService;
    private final String PUBLIC_KEY ="union:bank:oauth2:auth:server";

    public String generateToken(String clientId, String publicKey) throws AuthServerException {
        String token = UUID.randomUUID().toString();
        if (!Validation.validData(publicKey) ){
            log.info("Invalid public key was sent");
            throw new AuthServerException(400,"Invalid public key was sent");
        }
        if (!PUBLIC_KEY.equals(publicKey)){
            log.info("Public key does not match");
            throw new AuthServerException(400,"Public key does not match");
        }
        if (!Validation.validData(clientId)) {
            log.info("ClientId request is empty");
            throw new AuthServerException(400,"ClientId request is empty");
        }
        boolean saved = redisService.saveRecordToRedis(token, clientId, 30);
        if (!saved){
            log.info("Generated token not saved");
            throw new AuthServerException(400,"Generated token not saved");
        }
        return token;
    }

    public boolean validateToken(String clientId,String token,String publicKey) throws AuthServerException {

        if (!Validation.validData(publicKey) || !PUBLIC_KEY.equals(publicKey)){
            log.info("Invalid public key was sent. Stored {} , request {}",PUBLIC_KEY,publicKey);
            throw new AuthServerException(401,"Invalid public key");
        }
        if (!Validation.validData(token)) {
            log.info("token request is empty");
            throw new AuthServerException(401,"Token is empty");
        }
        if (!Validation.validData(clientId)) {
            log.info("ClientId request is empty");
            return false;
        }
        String recordFromRedis = redisService.getRecordFromRedis(clientId, String.class);
        if (recordFromRedis ==null) {
            log.info("No token found on the cache");
            throw new AuthServerException(401,"Token expired or invalid");
        }
        if (!recordFromRedis.equals(token)){
            log.info("Token does not match cached {} sent {}",recordFromRedis,token);
            throw new AuthServerException(401,"Token is invalid");
        }
        return true;
    }


    public Response validateInternalCall(HttpServletRequest httpServletRequest) {
        String clientId = httpServletRequest.getHeader("ClientId");
        String publicKey = httpServletRequest.getHeader("PublicKey");
        String token = httpServletRequest.getHeader("Authorization");

        log.info("The request headers ClientId {}: PublicKey : {} Token {} ",clientId,publicKey,token);

        boolean valid = false;
        try {
            valid = validateToken(clientId, token, publicKey);
        } catch (AuthServerException e) {
            e.printStackTrace();
            return new Response(e.getHttpCode(),e.getMessage(),null);
        }
        if (!valid){
            log.info("{}",new Response(401,"Invalid access token",null));
            return new Response(401,"Invalid access token",null);
        }
        return new Response(202,"Access Token is valid",null);
    }
}
