package tn.request.domain.auth;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import tn.request.bazooka.Bazooka;
import tn.request.bazooka.opt.BazookaOpt;
import tn.request.data.auth.ConfirmationTokenEntity;
import tn.request.data.auth.ConfirmationTokenRepository;
import tn.request.data.user.UserEntity;
import tn.request.data.user.UserRepository;
import tn.request.domain.auth.mail.ConfirmationEmailSender;
import tn.request.domain.auth.mapper.UserMapper;
import tn.request.domain.auth.model.LoginData;
import tn.request.domain.auth.model.UserRegistrationData;
import tn.request.domain.user.exception.InvalidConfirmationTokenException;
import tn.request.domain.user.exception.UserAlreadyExistException;
import tn.request.domain.user.exception.UserNotFoundException;

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
    public void registerUser(@NonNull UserRegistrationData registrationData) {
        Objects.requireNonNull(registrationData.getEmail());
        Objects.requireNonNull(registrationData.getFirstname());
        Objects.requireNonNull(registrationData.getLastname());

        Bazooka.checkIf(userRepository.existsByEmail(registrationData.getEmail()))
                .thenThrow(new UserAlreadyExistException(registrationData.getEmail()));

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
        Optional<ConfirmationTokenEntity> confirmationTokenOpt =
                confirmationTokenRepository.findByToken(token);

        ConfirmationTokenEntity confirmationToken =
                BazookaOpt.checkIfEmpty(confirmationTokenOpt)
                        .thenThrow(new InvalidConfirmationTokenException("Invalid Token: " + token))
                        .orElseGet();

        Bazooka.checkIf(confirmationToken.isExpired())
                .thenThrow(new InvalidConfirmationTokenException("Token Expired: " + confirmationToken));

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

    public UserEntity login(LoginData loginData) {
        Objects.requireNonNull(loginData);

        Bazooka.checkIfNot(userRepository.existsByEmail(loginData.getEmail()))
                .thenThrow(new UserNotFoundException());
        UserEntity userEntity = userRepository.getUserEntityByEmail(loginData.getEmail());

        Bazooka.checkIfNot(userEntity.isVerified())
                .thenThrow(new AuthorizationServiceException("User not verified"));

        return userEntity;
    }
}
