package net.javaguides.springboot.service.EmailSender;

import com.twilio.rest.lookups.v1.PhoneNumber;

public interface NotificationService {
    void sendMail(String to, String[] cc, String subject, String body);

    void sendMessage(String phoneNumber, String body);

    void verifyPhoneNumber(String phoneNumberTBV);
}
