package tn.request.app.auth.api;

import java.util.regex.Pattern;

import com.google.common.base.Strings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
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
import tn.request.app.exceptions.InvalidEmailFormatException;
import tn.request.bazooka.Bazooka;
import tn.request.data.user.UserEntity;
import tn.request.service.auth.UserRegistrationService;
import tn.request.service.user.exception.InvalidConfirmationTokenException;
import tn.request.service.user.exception.UserAlreadyExistException;
import tn.request.service.user.exception.UserNotFoundException;

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
    public ResponseEntity<Object> register(@RequestBody UserRegistrationRequest request) {
        try {
            Bazooka.checkIfNot(isEmailValid(request.getEmail()))
                   .thenThrow(new InvalidEmailFormatException("Invalid Email: " + request.getEmail()));
            if (!isPasswordValid(request.getPassword())) {
                return ResponseEntity.badRequest().build();
            }
            registrationService.registerUser(userRegistrationMapper.userRegistrationRequestToUserRegistrationData(request));
            return ResponseEntity.ok(request);
        } catch (InvalidEmailFormatException invalidEmailFormatException) {
            log.error(invalidEmailFormatException.toString());
            return new ResponseEntity<>("Invalid Email", HttpStatus.BAD_REQUEST);
        } catch (UserAlreadyExistException userAlreadyExistException) {
            log.error(userAlreadyExistException.toString());
            return new ResponseEntity<>(String.format("Email '%s' already exists, please select another email",
                    request.getEmail()), HttpStatus.CONFLICT);
        } catch (Exception exception) {
            log.error(exception.toString());
        }
        return ResponseEntity.badRequest()
                             .build();
    }

    @Operation(summary = "Confirm user email")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = MimeTypeUtils.TEXT_HTML_VALUE)})
    @ApiResponse(responseCode = "400", description = "Token is either invalid or expired", content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE)})
    @GetMapping("/confirmRegistration")
    public ResponseEntity<Object> confirmEmail(@RequestParam("token") String token) {
        try {
            Bazooka.checkIf(Strings.isNullOrEmpty(token))
                   .thenThrow(new IllegalArgumentException("Confirmation token cannot be empty"));

            registrationService.confirmEmail(token);
            return ResponseEntity.ok("<h1>Registration Confirmed Successfully</h1");
        } catch (IllegalArgumentException | InvalidConfirmationTokenException exception) {
            log.error(exception.toString());
            return ResponseEntity.badRequest()
                                 .body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        try {
            Bazooka.checkIf(isEmailValid(request.getEmail()))
                   .thenThrow(new InvalidEmailFormatException("Invalid Email: " + request.getEmail()));
            UserEntity userData = registrationService.login(loginMapper.loginRequestToLoginData(request));
            return ResponseEntity.ok(userData);
        } catch (InvalidEmailFormatException invalidEmailFormatException) {
            log.error(invalidEmailFormatException.toString());
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException userNotFoundException) {
            log.error(userNotFoundException.toString());
            return ResponseEntity.notFound().build();
        } catch (AuthorizationServiceException authorizationServiceException) {
            log.error(authorizationServiceException.toString());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Based on OWASP validation regular expression
    private boolean isEmailValid(String email) {
        String regexEmail = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(regexEmail)
                       .matcher(email)
                       .matches();
    }

    private boolean isPasswordValid(String password) {
        return !password.isEmpty() &&
                password.chars().noneMatch(Character::isWhitespace) &&
                password.length() >= 8 && password.length() <= 24;
    }
}
