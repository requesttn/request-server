package tn.request.app.auth.dto;

import lombok.Data;
import tn.request.service.auth.model.User;

@Data
public class LoginResponse {
    private final String jwt;
    private final User user;
}
