package market.henry.auth.config.email.email;

import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ToString
public class EmailMessage implements Serializable {


    private String to_address;
    private String subject;
    private String body;




    public String getTo_address() {
        return to_address;
    }

    public EmailMessage setTo_address(String to_address) {
        this.to_address = to_address;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public EmailMessage setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getBody() {
        return body;
    }

    public EmailMessage setBody(String body) {
        this.body = body;
        return this;
    }
}
