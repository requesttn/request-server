package tn.request.service.auth.mapper;

import org.springframework.stereotype.Component;
import tn.request.data.user.UserEntity;
import tn.request.service.auth.model.UserRegistrationData;

@Component
public class UserMapper {
    public UserRegistrationData to(UserEntity userEntity) {
        return new UserRegistrationData(
                userEntity.getFirstname(), userEntity.getLastname(), userEntity.getEmail(), userEntity.getPassword());
    }

    /**
     * @return an unverified user with a {@code null} id and the same {@code firstname}, {@code
     * lastname} and {@code email} as {@code userData}
     */
    public UserEntity from(UserRegistrationData userData) {
        return new UserEntity(
                null, userData.getFirstname(), userData.getLastname(), userData.getEmail(), userData.getPassword(), false);
    }
}
