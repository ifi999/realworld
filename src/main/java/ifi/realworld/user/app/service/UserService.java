package ifi.realworld.user.app.service;

import ifi.realworld.common.exception.AlreadyExistedUserException;
import ifi.realworld.user.api.UserPasswordEncoder;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserPasswordEncoder passwordEncoder;

    public User createUser(UserCreateRequest dto) {
        Optional<User> checkDuplicatedUser = userRepository.findByEmailAndUsername(dto.getEmail(), dto.getUsername());
        if (checkDuplicatedUser.isPresent()) {
            throw new AlreadyExistedUserException(dto.getEmail());
        }

        User user = User.builder()
                        .email(dto.getEmail())
                        .username(dto.getUsername())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .passwordEncoder(passwordEncoder)
                        .build();

        return userRepository.save(user);
    }

}
