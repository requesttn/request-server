package tn.request.service.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import tn.request.bazooka.opt.BazookaOpt;
import tn.request.data.auth.ConfirmationTokenEntity;
import tn.request.data.auth.ConfirmationTokenRepository;
import tn.request.data.user.UserEntity;
import tn.request.data.user.UserRepository;
import tn.request.service.auth.mail.ConfirmationEmailSender;
import tn.request.service.auth.mapper.UserRegistrationDetailsMapper;
import tn.request.service.auth.model.UserRegistrationDetails;
import tn.request.service.question.exception.RequestException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private UserRepository userRepository;
    private ConfirmationEmailSender confirmationEmailSender;
    private UserRegistrationDetailsMapper registrationDetailsMapper;
    private ConfirmationTokenRepository confirmationTokenRepository;

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtService jwtService;

    /**
     * Save user data in the database and send a confirmation link to his email
     */
    public void register(@NonNull UserRegistrationDetails registrationDetails) {
        Objects.requireNonNull(registrationDetails.getEmail(), "Email is required");
        Objects.requireNonNull(registrationDetails.getFirstname(), "Firstname is required");
        Objects.requireNonNull(registrationDetails.getLastname(), "Lastname is required");

        if (isEmailInvalid(registrationDetails.getEmail())) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Email is invalid");
        }

        if (userRepository.existsByEmail(registrationDetails.getEmail())) {
            throw new RequestException(HttpStatus.CONFLICT, "A user with the same email already exist");
        }

        if (isPasswordInvalid(registrationDetails.getPassword())) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Password is invalid");
        }

        UserEntity userEntity = userRepository.save(registrationDetailsMapper.toUserEntity(registrationDetails));

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
     * Ensure token is valid an not expired
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
        String generatedToken = UUID.randomUUID().toString();

        confirmationEmailSender.send(user.getEmail(), generatedToken);
        ConfirmationTokenEntity confirmationToken =
                new ConfirmationTokenEntity(null, generatedToken, user);
        confirmationTokenRepository.save(confirmationToken);
    }

    public String login(String email, String password) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException exception) {
            log.error("Error while authenticating user {} ", email, exception);
            throw new RequestException(HttpStatus.FORBIDDEN, "Incorrect email or password");
        }

        final UserDetails authenticatedUserDetails = userDetailsService.loadUserByUsername(email);

        UserEntity userEntity = userRepository.getUserEntityByEmail(email);

        // TODO: Delegate this check to spring security
        if (!userEntity.isAccountActivated()) {
            throw new RequestException(HttpStatus.UNAUTHORIZED, "User account is not confirmed yet");
        }

        return jwtService.generateToken(authenticatedUserDetails);
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
