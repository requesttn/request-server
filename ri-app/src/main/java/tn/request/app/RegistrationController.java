package tn.request.app;

import java.util.regex.Pattern;

import com.google.common.base.Strings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.request.domain.user.exception.InvalidConfirmationTokenException;
import tn.request.domain.user.exception.UserAlreadyExistException;
import tn.request.domain.user.UserRegistrationData;
import tn.request.domain.user.UserRegistrationService;
import tn.request.bazooka.Bazooka;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class RegistrationController {

    private UserRegistrationService registrationService;

    @Operation(summary = "Create a new user and send a confirmation link to user email.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Invalid email format", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "409", description = "User already registered", content = {@Content(mediaType = "application/json")})
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserRegistrationData userData) {
        try {
            Bazooka.checkIf(isEmailNotValid(userData.getEmail()))
                    .thenThrow(new InvalidEmailFormatException("Invalid Email: " + userData.getEmail()));

            registrationService.registerUser(userData);
            return ResponseEntity.ok(userData);
        }
        catch (InvalidEmailFormatException invalidEmailFormatException) {
            log.error(invalidEmailFormatException.toString());
            return new ResponseEntity<>("Invalid Email", HttpStatus.BAD_REQUEST);
        }
        catch (UserAlreadyExistException userAlreadyExistException) {
            log.error(userAlreadyExistException.toString());
            return new ResponseEntity<>(String.format("Email '%s' already exists, please select another email",
                                                      userData.getEmail()), HttpStatus.CONFLICT);
        }
        catch (Exception exception) {
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
        }
        catch (IllegalArgumentException | InvalidConfirmationTokenException exception) {
            log.error(exception.toString());
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }

    // Based on OWASP validation regular expression
    private boolean isEmailNotValid(String email) {
        String regexEmail = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return !Pattern.compile(regexEmail)
                .matcher(email)
                .matches();
    }
}
