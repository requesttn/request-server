package tn.request.app.auth.mapper;

import org.springframework.stereotype.Component;
import tn.request.app.auth.dto.LoginRequest;
import tn.request.service.auth.model.LoginData;

@Component
public class LoginCredentialsMapper {

    public LoginData loginRequestToLoginData(LoginRequest loginRequest) {
        return new LoginData(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
