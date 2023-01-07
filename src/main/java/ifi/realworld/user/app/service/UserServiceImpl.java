package ifi.realworld.user.app.service;

import ifi.realworld.common.exception.AlreadyExistedUserException;
import ifi.realworld.common.exception.InvalidEmailException;
import ifi.realworld.common.exception.PasswordNotMatchedException;
import ifi.realworld.user.api.UserPasswordEncoder;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserLoginDto;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserPasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserCreateRequest dto) {
        Optional<User> checkDuplicatedUser = userRepository.findByEmailAndUsername(dto.getEmail(), dto.getUsername());
        if (checkDuplicatedUser.isPresent()) {
            throw new AlreadyExistedUserException(dto.getEmail());
        }

        User user = User.builder()
                        .email(dto.getEmail())
                        .username(dto.getUsername())
                        .password(dto.getPassword())
                        .passwordEncoder(passwordEncoder)
                        .build();

        return userRepository.save(user);
    }

    @Override
    public User login(UserLoginDto dto) {
        Optional<User> findByEmail = userRepository.findByEmail(dto.getEmail());
        if (findByEmail.isEmpty()) {
            throw new InvalidEmailException(dto.getEmail());
        }

        User findUserByEmail = findByEmail.get();
        boolean matched = findUserByEmail.isMatched(dto.getPassword(), findUserByEmail.getPassword(), passwordEncoder);
        if (!matched) {
            throw new PasswordNotMatchedException(dto.getEmail());
        }

        return findUserByEmail;
    }
}
