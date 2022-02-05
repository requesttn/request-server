package tn.request.authentication.user;

import org.springframework.stereotype.Component;
import tn.request.data.user.UserEntity;

@Component
public class UserEntityMapper {
    public UserRegistrationData to(UserEntity userEntity) {
        return new UserRegistrationData(userEntity.getFirstname(), userEntity.getLastname(), userEntity.getEmail());
    }

    public UserEntity from(UserRegistrationData userData) {
        return new UserEntity(null, userData.getFirstname(), userData.getLastname(), userData.getEmail());
    }
}
