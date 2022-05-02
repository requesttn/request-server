package tn.request.service.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tn.request.bazooka.opt.BazookaOpt;
import tn.request.data.user.UserRepository;
import tn.request.service.auth.mapper.UserMapper;
import tn.request.service.auth.model.User;
import tn.request.service.question.exception.RequestException;

@AllArgsConstructor
@Service
public class UserService {

    private UserMapper userMapper;
    private UserRepository userRepository;

    public User getUserById(@NonNull Long id) {
        return userMapper.fromUserEntity(BazookaOpt.checkIfEmpty(userRepository.findById(id))
                .thenThrow(new RequestException(HttpStatus.NOT_FOUND, "The provided id doesn't correspond to any user"))
                .orElseGet());
    }
}
