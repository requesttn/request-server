package tn.request.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tn.request.service.auth.model.LoginData;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(LoginData userLoginData) {
        this(userLoginData.getEmail(), userLoginData.getPassword());
    }
}
