package tn.request.domain.user;

import org.springframework.stereotype.Component;
import tn.request.data.user.UserEntity;
import tn.request.domain.auth.UserRegistrationData;

@Component
public class UserEntityMapper {
    public UserRegistrationData to(UserEntity userEntity) {
        return new UserRegistrationData(
                userEntity.getFirstname(), userEntity.getLastname(), userEntity.getEmail());
    }

    /**
     * @return an unverified user with a {@code null} id and the same {@code firstname}, {@code
     * lastname} and {@code email} as {@code userData}
     */
    public UserEntity from(UserRegistrationData userData) {
        return new UserEntity(
                null, userData.getFirstname(), userData.getLastname(), userData.getEmail(), false);
    }
}
