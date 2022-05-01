package tn.request.service.auth.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import tn.request.data.user.UserEntity;
import tn.request.service.auth.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(User user);

    User fromUserEntity(UserEntity userEntity);
}
