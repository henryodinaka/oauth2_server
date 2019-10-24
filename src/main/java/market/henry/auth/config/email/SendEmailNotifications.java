package market.henry.auth.config.email;


import lombok.extern.slf4j.Slf4j;
import market.henry.auth.config.email.email.EmailMessage;
import market.henry.auth.config.email.email.EmailMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
@Slf4j
public class SendEmailNotifications {
    @Autowired
    private EmailMessage emailMessage;
    @Autowired
    private EmailMessageService emailMessageService;


    public void sendApprovalMail(String userEmail, String otp,String expires) {
        String details ="<p> Hello </p> </br>";
        String subject = "One Time Password";
        String subtitle ="";
        String title = "";
        String URL = "";
        String applicationName ="GSI ";
                details += "<p>Your One Time Password (OTP) is "+otp+"</p>"+
                        "<p>OTP is only valid for"+ expires +"minutes</p>"+
                        "<p>Keep it save from unauthorized access</p>";

        // TODO: 8/9/2019 Remember to uncomment this when deploying to nibss server
        //Send the mail
       /* smtpMailSender.sendMail(fromEmail, userEmail.getEmail(), subject, title, subtitle, details);*/
        EmailMessage emailMessage = this.emailMessage.
                setBody(details)
                .setSubject(subject)
                .setTo_address(userEmail);
        send(emailMessage);
    }

    public void send(EmailMessage emailMessage) {
        try {
            emailMessageService.sendMail(emailMessage);
        } catch (MessagingException e) {
            log.error("An error Occured while sending mail ",e);
        }
    }


}