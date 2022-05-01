package tn.request.service.auth;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tn.request.bazooka.opt.BazookaOpt;
import tn.request.data.auth.ConfirmationTokenEntity;
import tn.request.data.auth.ConfirmationTokenRepository;
import tn.request.data.user.UserEntity;
import tn.request.data.user.UserRepository;
import tn.request.service.auth.mail.ConfirmationEmailSender;
import tn.request.service.auth.mapper.UserMapper;
import tn.request.service.auth.model.User;
import tn.request.service.question.exception.RequestException;

@Service
@AllArgsConstructor
@Slf4j
public class UserRegistrationService {

    private UserRepository userRepository;

    private ConfirmationEmailSender confirmationEmailSender;

    private UserMapper userEntityMapper;

    private ConfirmationTokenRepository confirmationTokenRepository;

    /**
     * Register a new user and start the verification process
     */
    public void registerUser(@NonNull User registrationData) {
        Objects.requireNonNull(registrationData.getEmail(), "Email is required");
        Objects.requireNonNull(registrationData.getFirstname(), "Firstname is required");
        Objects.requireNonNull(registrationData.getLastname(), "Lastname is required");

        if (isEmailInvalid(registrationData.getEmail())) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Email is invalid");
        }

        if (userRepository.existsByEmail(registrationData.getEmail())) {
            throw new RequestException(HttpStatus.CONFLICT, "A user with the same email already exist");
        }

        if (isPasswordInvalid(registrationData.getPassword())) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Password is invalid");
        }

        UserEntity user = userRepository.save(userEntityMapper.from(registrationData));
        CompletableFuture.runAsync(() -> sendConfirmationEmailTo(user))
                         .handleAsync((unused, throwable) -> {
                             if (throwable == null) {
                                 log.info("Confirmation email sent successfully to '{}'", user.getEmail());
                             } else {
                                 log.error(
                                         "Error while sending confirmation email to '{}': {}",
                                         user.getEmail(),
                                         throwable.toString());
                             }
                             return null;
                         });
    }

    public void confirmEmail(@NonNull String token) {
        Objects.requireNonNull(token, "Confirmation token is required");

        Optional<ConfirmationTokenEntity> confirmationTokenOpt =
                confirmationTokenRepository.findByToken(token);

        ConfirmationTokenEntity confirmationToken =
                BazookaOpt.checkIfEmpty(confirmationTokenOpt)
                          .thenThrow(new RequestException(HttpStatus.BAD_REQUEST, "Account confirmation token is invalid"))
                          .orElseGet();

        if (confirmationToken.isExpired()) {
            throw new RequestException("Account confirmation token is expired");
        }

        doConfirmEmail(confirmationToken);
    }

    private void doConfirmEmail(ConfirmationTokenEntity confirmationToken) {
        Objects.requireNonNull(confirmationToken);
        Objects.requireNonNull(confirmationToken.getUser());
        Objects.requireNonNull(confirmationToken.getToken());

        UserEntity confirmedUser = confirmationToken.getUser();
        confirmedUser.setVerified(true);
        userRepository.save(confirmedUser);

        confirmationTokenRepository.delete(confirmationToken);

        log.info("Email Confirmed Successfully: " + confirmationToken.getUser().getEmail());
    }

    private void sendConfirmationEmailTo(UserEntity user) {
        String generatedToken = UUID.randomUUID()
                                    .toString();
        confirmationEmailSender.send(user.getEmail(), generatedToken);
        ConfirmationTokenEntity confirmationToken =
                new ConfirmationTokenEntity(null, generatedToken, user);
        confirmationTokenRepository.save(confirmationToken);
    }

    public UserEntity login(String email, String password) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);

        if (!userRepository.existsByEmail(email)) {
            throw new RequestException(HttpStatus.NOT_FOUND, "The provided email doesn't belong to any user");
        }

        UserEntity userEntity = userRepository.getUserEntityByEmail(email);

        if (!userEntity.isVerified()) {
            throw new RequestException(HttpStatus.UNAUTHORIZED, "User account is not confirmed yet");
        }

        return userEntity;
    }

    // Based on OWASP validation regular expression
    private boolean isEmailInvalid(String email) {
        String regexEmail = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return !Pattern.compile(regexEmail)
                       .matcher(email)
                       .matches();
    }

    private boolean isPasswordInvalid(String password) {
        return !(!password.isEmpty() &&
                password.chars().noneMatch(Character::isWhitespace) &&
                password.length() >= 8 && password.length() <= 24);
    }
}
