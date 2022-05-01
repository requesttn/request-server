package tn.request.app.auth.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.request.app.auth.dto.LoginRequest;
import tn.request.app.auth.dto.RegisterUserRequest;
import tn.request.app.auth.mapper.UserRegistrationMapper;
import tn.request.service.auth.AuthenticationService;
import tn.request.service.auth.model.User;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class AuthenticationController implements IAuthenticationController {

    private AuthenticationService authenticationService;
    private UserRegistrationMapper userRegistrationMapper;

    @Override
    @PostMapping("/register")
    public ResponseEntity<RegisterUserRequest> register(@RequestBody RegisterUserRequest request) {
        authenticationService.register(userRegistrationMapper.toUser(request));
        return ResponseEntity.ok(request);
    }

    @Override
    @GetMapping("/confirmRegistration")
    public ResponseEntity<Object> activateAccount(@RequestParam("token") String token) {
        authenticationService.validateTokenAndActivateAccount(token);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        User loginResponse = authenticationService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(loginResponse);
    }
}
