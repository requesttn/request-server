package tn.request.app.auth.mapper;

import org.mapstruct.Mapper;
import tn.request.app.auth.dto.RegisterUserRequest;
import tn.request.service.auth.model.User;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {
    User toUser(RegisterUserRequest request);
}
