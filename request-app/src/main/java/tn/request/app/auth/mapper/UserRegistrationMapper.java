package tn.request.app.auth.mapper;

import org.mapstruct.Mapper;
import tn.request.app.auth.dto.UserRegistrationRequest;
import tn.request.service.auth.model.UserRegistrationDetails;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {
    UserRegistrationDetails toUserRegistrationDetails(UserRegistrationRequest request);
}
