package tn.request.app.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.request.service.user.dto.UserDto;

public interface IUserController {
    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUser(@PathVariable Long id);
}
