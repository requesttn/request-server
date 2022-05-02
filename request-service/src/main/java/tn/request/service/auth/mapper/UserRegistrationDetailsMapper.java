package tn.request.service.auth.mapper;

import org.mapstruct.Mapper;
import tn.request.data.user.UserEntity;
import tn.request.service.auth.model.UserRegistrationDetails;

@Mapper(componentModel = "spring")
public interface UserRegistrationDetailsMapper {
    UserEntity toUserEntity(UserRegistrationDetails registrationDetails);
}
