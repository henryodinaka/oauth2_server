package market.henry.auth.exceptions;

import lombok.Data;

@Data
public class AuthServerException extends Exception {
    private Integer httpCode;
    public AuthServerException(Integer httpCode, String message)
    {
        super(message);
        this.httpCode = httpCode;
    }
}