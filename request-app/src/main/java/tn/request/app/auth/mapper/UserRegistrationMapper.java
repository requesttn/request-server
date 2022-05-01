package tn.request.app.auth.mapper;

import org.springframework.stereotype.Component;
import tn.request.app.auth.dto.UserRegistrationRequest;
import tn.request.service.auth.model.User;

@Component
public class UserRegistrationMapper {
    public User userRegistrationRequestToUserRegistrationData(UserRegistrationRequest request) {
        return new User(request.getFirstname(), request.getLastname(), request.getEmail(), request.getPassword());
    }
}
