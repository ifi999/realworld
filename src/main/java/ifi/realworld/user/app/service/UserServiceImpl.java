package ifi.realworld.user.app.service;

import ifi.realworld.common.exception.AlreadyExistedUserException;
import ifi.realworld.common.exception.InvalidEmailException;
import ifi.realworld.common.exception.PasswordNotMatchedException;
import ifi.realworld.common.exception.UserNotFoundException;
import ifi.realworld.common.security.CustomUserDetailsService;
import ifi.realworld.common.security.JwtProvider;
import ifi.realworld.user.api.UserPasswordEncoder;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserLoginDto;
import ifi.realworld.user.api.dto.UserUpdateRequest;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public User createUser(UserCreateRequest dto) {
        Optional<User> checkDuplicatedUser = userRepository.findByEmailOrUsername(dto.getEmail(), dto.getUsername());
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

        User user = findUser.orElseThrow(() -> new UserNotFoundException(dto.getEmail() + " not found."));
        boolean matched = user.isMatched(dto.getPassword(), user.getPassword(), passwordEncoder);
        if (!matched) {
            throw new PasswordNotMatchedException(user.getEmail());
        }

        createToken(user, response);
        return user;
    }

    @Override
    public User getCurrentUserInfo() {
        return getCurrentUser();
    }

    @Override
    public User updateUser(UserUpdateRequest dto, HttpServletResponse response) {
        Optional<User> findEmail = userRepository.findByEmailOrUsername(dto.getEmail(), dto.getUsername());
        if (findEmail.isPresent()) {
            throw new AlreadyExistedUserException("Email or Name");
        }
        User currentUser = getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new UserNotFoundException("User ID : " + currentUser.getId() + " not found."));
        user.changeInfo(
                dto.getUsername(), dto.getEmail()
                , dto.getPassword(), passwordEncoder
                , dto.getBio(), dto.getImage()
        ); // TODO - ???????????? ?????? ??? ?????? ??? ????????? ????????????. ????????? ????????? .. dto??? ??????????????? Entity??? ?????? dto??? ???????????? ??????

        if (!currentUser.getUsername().equals(dto.getUsername())) {
            this.createNewAuthentication(user, response);
        }

        return user;
    }

    private User getCurrentUser() {
        UserDetails userDetails = customUserDetailsService.getCurrentUserDetails();
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email + " not found."));
    }

    public void createNewAuthentication(User user, HttpServletResponse response) {
        UserDetails userDetails = customUserDetailsService.loadUserById(user.getId());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList()));
        this.createToken(user, response);
    }

    private void createToken(User user, HttpServletResponse response) {
        final String token = jwtProvider.createToken(user.getEmail());
        this.saveTokenInCookie(token, response);
    }

    private void saveTokenInCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("AccessToken", token);
        cookie.setMaxAge(60*60*24);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
