package tn.request.authentication.user;

import java.util.Objects;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConfirmationEmailSender {
  // TODO: Get app url dynamically
  private final String appUrl = "http://localhost:9555";

  private final JavaMailSender mailSender;

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
    return appUrl + "/api/v1/confirmRegistration?token=" + token;
  }
}
