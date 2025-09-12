package net.javaguides.springboot.service.EmailSender;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;
import net.javaguides.springboot.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final String DEFAULT_REGION = "IN";

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Value("${twilio.from_number}")
    private String TWILIO_FROM;

    @Autowired
    private JavaMailSender javaMailSender;

    // ----------------------------
    // EMAIL
    // ----------------------------
    @Override
    public void sendMail(String to, String[] cc, String subject, String body) {
        if (to == null || to.trim().isEmpty()) {
            log.warn("sendMail: empty 'to' address, skipping");
            return;
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(to);
            if (cc != null && cc.length > 0) mailMessage.setCc(cc);
            mailMessage.setText(body);
            mailMessage.setSubject(subject);

            javaMailSender.send(mailMessage);
            log.info("Email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }

    // ----------------------------
    // SMS (Twilio) - sendMessage
    // ----------------------------
    @Override
    public void sendMessage(String toPhoneNumber, String body) {
        if (toPhoneNumber == null || toPhoneNumber.trim().isEmpty()) {
            log.warn("sendMessage called with empty recipient");
            return;
        }

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String toE164;
        try {
            toE164 = normalizeToE164(toPhoneNumber, DEFAULT_REGION);
        } catch (NumberParseException | IllegalArgumentException ex) {
            log.error("Invalid phone number '{}', skipping SMS: {}", toPhoneNumber, ex.getMessage());
            return;
        }

        try {
            Message.creator(new PhoneNumber(toE164),
                            new PhoneNumber(TWILIO_FROM),
                            body)
                    .create();
            log.info("SMS sent to {}", maskNumber(toE164));
        } catch (ApiException ae) {
            log.error("Twilio API error sending SMS to {}: status={}, message={}",
                    maskNumber(toE164), ae.getStatusCode(), ae.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error sending SMS to {}: {}", maskNumber(toE164), e.getMessage(), e);
        }
    }

    // ----------------------------
    // Phone number verification using Twilio Lookup
    // ----------------------------
    @Override
    public void verifyPhoneNumber(String phoneNumberTBV) {
        if (phoneNumberTBV == null || phoneNumberTBV.trim().isEmpty()) {
            log.warn("verifyPhoneNumber called with empty input");
            return;
        }

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String toE164;
        try {
            toE164 = normalizeToE164(phoneNumberTBV, DEFAULT_REGION);
        } catch (NumberParseException | IllegalArgumentException ex) {
            log.error("verifyPhoneNumber: invalid number '{}': {}", phoneNumberTBV, ex.getMessage());
            return;
        }

        try {
            // Use fully-qualified name for the lookup result class to avoid import conflict
            com.twilio.rest.lookups.v1.PhoneNumber fetched =
                    com.twilio.rest.lookups.v1.PhoneNumber.fetcher(new PhoneNumber(toE164))
                            .fetch();

            log.info("Lookup result for {}: country={}, carrier={}, callerName={}",
                    maskNumber(toE164),
                    fetched.getCountryCode(),
                    fetched.getCarrier(),
                    fetched.getCallerName());
        } catch (ApiException a) {
            log.error("Twilio Lookup API error for {}: {}", maskNumber(toE164), a.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during Twilio lookup for {}: {}", maskNumber(toE164), e.getMessage(), e);
        }
    }

    // ----------------------------
    // Helpers
    // ----------------------------
    private String normalizeToE164(String raw, String defaultRegion) throws NumberParseException {
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber pn = util.parse(raw, defaultRegion);
        if (!util.isValidNumber(pn)) {
            throw new IllegalArgumentException("Not a valid phone number");
        }
        return util.format(pn, PhoneNumberUtil.PhoneNumberFormat.E164);
    }

    private String maskNumber(String e164) {
        if (e164 == null) return null;
        int len = e164.length();
        if (len <= 6) return e164;
        return e164.substring(0, 3) + "*****" + e164.substring(len - 3);
    }
}
