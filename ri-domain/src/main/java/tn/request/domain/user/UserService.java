package tn.request.domain.user;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tn.request.bazooka.opt.BazookaOpt;
import tn.request.data.user.UserRepository;
import tn.request.domain.user.dto.UserDto;
import tn.request.domain.user.exception.UserNotFoundException;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;

    public UserDto getUserById(@NonNull Long id) {
        return UserDto.fromEntity(BazookaOpt.checkIfEmpty(userRepository.findById(id))
                                          .thenThrow(new UserNotFoundException())
                                          .orElseGet());
    }
}
