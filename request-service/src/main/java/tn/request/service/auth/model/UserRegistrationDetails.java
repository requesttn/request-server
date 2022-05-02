package tn.request.service.auth.model;

import lombok.Data;

@Data
public class UserRegistrationDetails {
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String password;
}
