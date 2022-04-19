package tn.request.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tn.request.data.user.UserEntity;

@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;

    public static UserDto fromEntity(UserEntity userEntity) {
        return new UserDto(userEntity.getId(), userEntity.getFirstname(), userEntity.getLastname(), userEntity.getEmail());
    }
}
