package tn.request.app.auth.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.request.app.auth.dto.LoginRequest;
import tn.request.app.auth.dto.LoginResponse;
import tn.request.app.auth.dto.UserRegistrationRequest;
import tn.request.app.auth.mapper.UserRegistrationMapper;
import tn.request.service.auth.AuthenticationService;
import tn.request.service.user.UserService;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class AuthenticationController implements IAuthenticationController {

    private AuthenticationService authenticationService;

    private UserService userService;
    private UserRegistrationMapper userRegistrationMapper;

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationRequest> register(@RequestBody UserRegistrationRequest request) {
        authenticationService.register(userRegistrationMapper.toUserRegistrationDetails(request));
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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String jwt = authenticationService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(jwt, userService.getUserByEmail(request.getEmail())));
    }
}
