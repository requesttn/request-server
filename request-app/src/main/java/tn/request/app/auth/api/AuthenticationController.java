package tn.request.app.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.request.app.auth.dto.LoginRequest;
import tn.request.app.auth.dto.UserRegistrationRequest;
import tn.request.app.auth.mapper.LoginCredentialsMapper;
import tn.request.app.auth.mapper.UserRegistrationMapper;
import tn.request.data.user.UserEntity;
import tn.request.service.auth.UserRegistrationService;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class AuthenticationController {

    private UserRegistrationService registrationService;
    private LoginCredentialsMapper loginMapper;
    private UserRegistrationMapper userRegistrationMapper;

    @Operation(summary = "Create a new user and send a confirmation link to user email.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Invalid email format", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "409", description = "User already registered", content = {@Content(mediaType = "application/json")})
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationRequest> register(@RequestBody UserRegistrationRequest request) {
        registrationService.registerUser(userRegistrationMapper.userRegistrationRequestToUserRegistrationData(request));
        return ResponseEntity.ok(request);
    }

    @Operation(summary = "Confirm user email")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = MimeTypeUtils.TEXT_HTML_VALUE)})
    @ApiResponse(responseCode = "400", description = "Token is either invalid or expired", content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE)})
    @GetMapping("/confirmRegistration")
    public ResponseEntity<Object> confirmEmail(@RequestParam("token") String token) {
        registrationService.confirmEmail(token);
        return ResponseEntity.ok("<h1>Registration Confirmed Successfully</h1");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        UserEntity loginResponse = registrationService.login(loginMapper.loginRequestToLoginData(request));
        return ResponseEntity.ok(loginResponse);
    }
}
