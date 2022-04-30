package tn.request.service.auth.mail;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationEmailSender {
    // TODO: Get app url dynamically
    @Value("${front-end.url}")
    private String frontEndUrl;

    private final JavaMailSender mailSender;

    public ConfirmationEmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String recipientEmail, String token) {
        Objects.requireNonNull(recipientEmail);
        Objects.requireNonNull(token);
        SimpleMailMessage confirmationEmail = new SimpleMailMessage();
        confirmationEmail.setTo(recipientEmail);
        confirmationEmail.setSubject("Action Required: Please confirm your email");
        confirmationEmail.setText(buildConfirmationLink(token));
        mailSender.send(confirmationEmail);
    }

    private String buildConfirmationLink(String token) {
        return frontEndUrl + "/account/claim?token=" + token;
    }
}
