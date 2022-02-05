package tn.request.authentication.user;

import java.util.Objects;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tn.request.authentication.UserAlreadyExistException;
import tn.request.data.user.UserRepository;

@Service
@AllArgsConstructor
public class UserRegistrationService {

    private UserRepository userRepository;

    private UserEntityMapper userEntityMapper;

    public void registerUser(@NonNull UserRegistrationData userData) {
        Objects.requireNonNull(userData.getEmail());
        Objects.requireNonNull(userData.getFirstname());
        Objects.requireNonNull(userData.getLastname());
        if (userRepository.existsByEmail(userData.getEmail())) {
            throw new UserAlreadyExistException(userData.getEmail());
        } else {
            userRepository.save(userEntityMapper.from(userData));
        }
    }
}
