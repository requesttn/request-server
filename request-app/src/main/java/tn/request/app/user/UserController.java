package tn.request.app.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.request.service.auth.model.User;
import tn.request.service.user.UserService;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController implements IUserController {

    private UserService userService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
