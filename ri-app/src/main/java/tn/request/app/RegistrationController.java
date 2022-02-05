package tn.request.app;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.request.authentication.user.UserRegistrationData;
import tn.request.authentication.user.UserRegistrationService;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class RegistrationController {

    private UserRegistrationService registrationService;

    @PostMapping("/register")
    public void register(UserRegistrationData userData) {

    }
}
