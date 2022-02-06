package tn.request.app;

import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.request.app.util.Exceptions;
import tn.request.authentication.UserAlreadyExistException;
import tn.request.authentication.user.UserRegistrationData;
import tn.request.authentication.user.UserRegistrationService;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class RegistrationController {

    private UserRegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserRegistrationData userData) {
        try {
            Exceptions.throwIf(isEmailNotValid(userData.getEmail()),
                    new InvalidEmailFormatException("Invalid Email: " + userData.getEmail()));
            registrationService.registerUser(userData);
            return ResponseEntity.ok(userData);
        } catch (InvalidEmailFormatException invalidEmailFormatException) {
            log.error(invalidEmailFormatException.toString());
            return new ResponseEntity<>("Invalid Email", HttpStatus.BAD_REQUEST);
        } catch (UserAlreadyExistException userAlreadyExistException) {
            log.error(userAlreadyExistException.toString());
            return new ResponseEntity<>(String.format("Email '%s' already exists, please select another email", userData.getEmail()),
                    HttpStatus.CONFLICT);
        } catch (Exception exception) {
            log.error(exception.toString());
        }
        return ResponseEntity.badRequest().build();
    }

    // Based on OWASP validation regular expression
    private boolean isEmailNotValid(String email) {
        String regexEmail = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return !Pattern.compile(regexEmail)
                       .matcher(email)
                       .matches();
    }
}
