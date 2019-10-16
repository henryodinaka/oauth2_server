package market.henry.auth.dto;

import lombok.Data;

@Data
public class SecretRequest {
    private String phoneNumber;
    private String secret;
}
