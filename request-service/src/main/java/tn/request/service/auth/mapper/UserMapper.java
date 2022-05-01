package tn.request.service.auth.mapper;

import org.springframework.stereotype.Component;
import tn.request.data.user.UserEntity;
import tn.request.service.auth.model.User;

@Component
public class UserMapper {
    public User to(UserEntity userEntity) {
        return new User(
                userEntity.getFirstname(), userEntity.getLastname(), userEntity.getEmail(), userEntity.getPassword());
    }

    /**
     * @return an unverified user with a {@code null} id and the same {@code firstname}, {@code
     * lastname} and {@code email} as {@code userData}
     */
    public UserEntity from(User userData) {
        return new UserEntity(
                null, userData.getFirstname(), userData.getLastname(), userData.getEmail(), userData.getPassword(), false);
    }
}
