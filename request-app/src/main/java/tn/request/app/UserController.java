package tn.request.app;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.request.domain.user.UserService;
import tn.request.domain.user.exception.UserNotFoundException;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        }
        catch (UserNotFoundException userNotFoundException) {
            log.error("User not found: " + id, userNotFoundException);
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            log.error("Unknown error", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
