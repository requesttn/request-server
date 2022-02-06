package tn.request.authentication.user;

import java.util.Objects;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tn.request.authentication.UserAlreadyExistException;
import tn.request.data.ConfirmationTokenEntity;
import tn.request.data.ConfirmationTokenRepository;
import tn.request.data.user.UserEntity;
import tn.request.data.user.UserRepository;

@Service
@AllArgsConstructor
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
        if (userRepository.existsByEmail(userData.getEmail())) {
            throw new UserAlreadyExistException(userData.getEmail());
        } else {
            UserEntity user = userRepository.save(userEntityMapper.from(userData));
            //TODO: Run this asynchronously
            sendConfirmationEmailTo(user);
        }
    }

    private void sendConfirmationEmailTo(UserEntity user) {
        String generatedToken = generateTokenString();
        confirmationEmailSender.send(user.getEmail(), generatedToken);
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(null, generatedToken, user);
        confirmationTokenRepository.save(confirmationToken);
    }

    private String generateTokenString() {
        return UUID.randomUUID().toString();
    }
}
