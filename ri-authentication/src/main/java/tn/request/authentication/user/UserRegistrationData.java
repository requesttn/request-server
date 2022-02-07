package tn.request.authentication.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegistrationData {
  private String firstname;
  private String lastname;
  private String email;
}
