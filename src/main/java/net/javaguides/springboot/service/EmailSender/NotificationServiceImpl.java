package net.javaguides.springboot.service.EmailSender;

import com.twilio.exception.ApiException;
import com.twilio.rest.lookups.v1.PhoneNumber;
import net.javaguides.springboot.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(String to, String[] cc, String subject, String body) {
            // Try block to check for exceptions

                // Creating a simple mail message
                SimpleMailMessage mailMessage
                        = new SimpleMailMessage();

                // Setting up necessary details
                mailMessage.setFrom(fromEmail);
                mailMessage.setTo(to);
                mailMessage.setText(body);
                mailMessage.setSubject(subject);

                // Sending the mail
                javaMailSender.send(mailMessage);
    }

    @Override
    public void sendMessage(String toPhoneNumber, String body){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+263"+toPhoneNumber),
                new com.twilio.type.PhoneNumber("+12315708326"),
                body).create();
    }

    @Override
    public void verifyPhoneNumber(String phoneNumberTBV){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        try{
            PhoneNumber phoneNumber =
                    PhoneNumber.fetcher(new com.twilio.type.PhoneNumber("+263"+phoneNumberTBV)).fetch();
            System.out.println(phoneNumber.getCarrier());
            System.out.println(phoneNumber.getCallerName());
        } catch(ApiException e){
            System.out.println("Phone number is invalid. Error: " + e.getMessage());
        }

    }
}
