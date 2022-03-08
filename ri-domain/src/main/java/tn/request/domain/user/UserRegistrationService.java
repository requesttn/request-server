package tn.request.domain.user;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tn.request.bazooka.Bazooka;
import tn.request.bazooka.opt.BazookaOpt;
import tn.request.data.ConfirmationTokenEntity;
import tn.request.data.ConfirmationTokenRepository;
import tn.request.data.user.UserEntity;
import tn.request.data.user.UserRepository;

@Service
@AllArgsConstructor
@Slf4j
public class UserRegistrationService {

    private UserRepository userRepository;

    private ConfirmationEmailSender confirmationEmailSender;

    private UserEntityMapper userEntityMapper;

    private ConfirmationTokenRepository confirmationTokenRepository;

    /**
     * Register a new user and start the verification process
     */
    public void registerUser(@NonNull UserRegistrationData userData) {
        Objects.requireNonNull(userData.getEmail());
        Objects.requireNonNull(userData.getFirstname());
        Objects.requireNonNull(userData.getLastname());

        Bazooka.checkIf(userRepository.existsByEmail(userData.getEmail()))
                .thenThrow(new UserAlreadyExistException(userData.getEmail()));

        UserEntity user = userRepository.save(userEntityMapper.from(userData));
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
}