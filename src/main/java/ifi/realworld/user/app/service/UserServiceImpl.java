package ifi.realworld.user.app.service;

import ifi.realworld.common.exception.AlreadyExistedUserException;
import ifi.realworld.common.exception.InvalidEmailException;
import ifi.realworld.common.exception.UserIdNotFoundException;
import ifi.realworld.common.security.JwtProvider;
import ifi.realworld.user.api.UserPasswordEncoder;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserLoginDto;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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
    public User login(UserLoginDto dto, HttpServletResponse response) {
        Optional<User> findUser = userRepository.findByEmail(dto.getEmail());
        if (findUser.isEmpty()) {
            throw new InvalidEmailException("Invalid " + dto.getEmail() + ".");
        }

        User user = findUser.get();
        user.isMatched(dto.getPassword(), user.getPassword(), passwordEncoder);
        createToken(user, response);
        return user;
    }

    @Override
    public User getUserInfo(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserIdNotFoundException());
    }

    public void createToken(User user, HttpServletResponse response) {
        final String token = jwtProvider.createToken(user.getEmail());
        saveTokenInCookie(response, token);
    }

    private void saveTokenInCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("AccessToken", token);
        cookie.setMaxAge(60*60*24);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
