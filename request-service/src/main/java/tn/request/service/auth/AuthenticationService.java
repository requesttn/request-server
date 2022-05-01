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
public class AuthenticationService {

    private UserRepository userRepository;

    private ConfirmationEmailSender confirmationEmailSender;

    private UserMapper userMapper;

    private ConfirmationTokenRepository confirmationTokenRepository;

    /**
     * Save
     * user
     * data
     * in
     * the
     * database
     * and
     * send
     * a
     * confirmation
     * link
     * to
     * his
     * email
     */
    public void register(@NonNull User user) {
        Objects.requireNonNull(user.getEmail(), "Email is required");
        Objects.requireNonNull(user.getFirstname(), "Firstname is required");
        Objects.requireNonNull(user.getLastname(), "Lastname is required");

        if (isEmailInvalid(user.getEmail())) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Email is invalid");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RequestException(HttpStatus.CONFLICT, "A user with the same email already exist");
        }

        if (isPasswordInvalid(user.getPassword())) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Password is invalid");
        }

        UserEntity userEntity = userRepository.save(userMapper.toUserEntity(user));

        CompletableFuture.runAsync(() -> sendConfirmationEmailTo(userEntity))
                         .handleAsync((unused, throwable) -> {
                             if (throwable == null) {
                                 log.info("Confirmation email sent successfully to '{}'", userEntity.getEmail());
                             } else {
                                 log.error(
                                         "Error while sending confirmation email to '{}': {}",
                                         userEntity.getEmail(),
                                         throwable.toString());
                             }
                             return null;
                         });
    }

    public void validateTokenAndActivateAccount(@NonNull String token) {
        Objects.requireNonNull(token, "Confirmation token is required");
        validateToken(token).ifPresent(this::activateAccount);
    }

    /**
     * Ensure
     * token
     * is
     * valid
     * and
     * not
     * expired
     */
    private Optional<ConfirmationTokenEntity> validateToken(String token) {
        Optional<ConfirmationTokenEntity> confirmationTokenOpt =
                confirmationTokenRepository.findByToken(token);

        ConfirmationTokenEntity confirmationToken =
                BazookaOpt.checkIfEmpty(confirmationTokenOpt)
                          .thenThrow(new RequestException(HttpStatus.BAD_REQUEST, "Account confirmation token is invalid"))
                          .orElseGet();

        if (confirmationToken.isExpired()) {
            throw new RequestException("Account confirmation token is expired");
        }
        return confirmationTokenOpt;
    }

    private void activateAccount(ConfirmationTokenEntity confirmationToken) {
        Objects.requireNonNull(confirmationToken);
        Objects.requireNonNull(confirmationToken.getUser());
        Objects.requireNonNull(confirmationToken.getToken());

        UserEntity confirmedUser = confirmationToken.getUser();
        confirmedUser.setAccountActivated(true);
        userRepository.save(confirmedUser);

        confirmationTokenRepository.delete(confirmationToken);

        log.info("Account '{}' activated successfully.", confirmationToken.getUser().getEmail());
    }

    private void sendConfirmationEmailTo(UserEntity user) {
        String generatedToken = UUID.randomUUID()
                                    .toString();
        confirmationEmailSender.send(user.getEmail(), generatedToken);
        ConfirmationTokenEntity confirmationToken =
                new ConfirmationTokenEntity(null, generatedToken, user);
        confirmationTokenRepository.save(confirmationToken);
    }

    public User login(String email, String password) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);

        if (!userRepository.existsByEmail(email)) {
            throw new RequestException(HttpStatus.NOT_FOUND, "The provided email doesn't belong to any user");
        }

        UserEntity userEntity = userRepository.getUserEntityByEmail(email);

        if (!userEntity.isAccountActivated()) {
            throw new RequestException(HttpStatus.UNAUTHORIZED, "User account is not confirmed yet");
        }

        return userMapper.fromUserEntity(userEntity);
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
