package tn.request.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {
    private String firstname;
    private String lastname;
    private String email;
    // TODO: Use an object to pass password and confirmed password. Then add password verification logic in the constructor
    private String password;
}
