package tn.request.app.auth.mapper;

import org.springframework.stereotype.Component;
import tn.request.app.auth.dto.UserRegistrationRequest;
import tn.request.service.auth.model.UserRegistrationData;

@Component
public class UserRegistrationMapper {
    public UserRegistrationData userRegistrationRequestToUserRegistrationData(UserRegistrationRequest request) {
        return new UserRegistrationData(request.getFirstname(), request.getLastname(), request.getEmail(), request.getPassword());
    }
}
