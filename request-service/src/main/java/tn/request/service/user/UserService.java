package tn.request.service.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tn.request.bazooka.opt.BazookaOpt;
import tn.request.data.user.UserRepository;
import tn.request.service.question.exception.RequestException;
import tn.request.service.user.dto.UserDto;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;

    public UserDto getUserById(@NonNull Long id) {
        return UserDto.fromEntity(BazookaOpt.checkIfEmpty(userRepository.findById(id))
                                            .thenThrow(new RequestException(HttpStatus.NOT_FOUND, "The provided id doesn't correspond to any user"))
                                            .orElseGet());
    }
}
