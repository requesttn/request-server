package tn.request.app.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.request.service.auth.model.User;

public interface IUserController {
    @GetMapping("/{id}")
    ResponseEntity<User> getUser(@PathVariable Long id);
}
