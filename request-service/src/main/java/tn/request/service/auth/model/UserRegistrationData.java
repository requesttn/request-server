package tn.request.service.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationData {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
