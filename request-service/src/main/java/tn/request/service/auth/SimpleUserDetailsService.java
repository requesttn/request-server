package tn.request.service.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.request.data.user.UserRepository;
import tn.request.service.auth.mapper.UserMapper;
import tn.request.service.user.exception.UserNotFoundException;

import java.util.Collections;

@Service
@AllArgsConstructor
public class SimpleUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserEntityByEmail(email)
                .map(userMapper::fromUserEntity)
                .map(user ->
                        User.withUsername(user.getEmail())
                                .password(user.getPassword())
                                .authorities(Collections.emptyList())
                                .build())
                .orElseThrow(UserNotFoundException::new);
    }
}
