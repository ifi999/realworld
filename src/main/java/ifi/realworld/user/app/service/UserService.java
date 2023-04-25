package ifi.realworld.user.app.service;

import ifi.realworld.utils.exception.api.AlreadyExistedUserException;
import ifi.realworld.utils.exception.api.InvalidEmailException;
import ifi.realworld.utils.exception.api.PasswordNotMatchedException;
import ifi.realworld.utils.exception.api.UserNotFoundException;
import ifi.realworld.utils.security.CustomUserDetailsService;
import ifi.realworld.utils.security.jwt.JwtProvider;
import ifi.realworld.user.api.UserPasswordEncoder;
import ifi.realworld.user.api.dto.*;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final String header;
    private final long validitySeconds;

    public UserService(UserRepository userRepository
            , UserPasswordEncoder passwordEncoder
            , JwtProvider jwtProvider
            , CustomUserDetailsService customUserDetailsService
            , @Value("${jwt.header}") String header
            , @Value("${jwt.validity-in-seconds}") long validitySeconds) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.header = header;
        this.validitySeconds = validitySeconds;
    }

    public UserCreateResponse createUser(UserCreateRequest dto) {
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
        return UserCreateResponse.of(userRepository.save(user));
    }

    public UserLoginDto login(UserLoginDto dto, HttpServletResponse response) {
        Optional<User> findUser = userRepository.findByEmail(dto.getEmail());
        if (findUser.isEmpty()) {
            throw new InvalidEmailException("Invalid " + dto.getEmail() + ".");
        }

        User user = findUser.orElseThrow(() -> new UserNotFoundException(dto.getEmail() + " not found."));
        confirmPassword(dto, user);
        createToken(user, response);

        return UserLoginDto.of(user);
    }

    public UserInfoDto getCurrentUserInfo() {
        return UserInfoDto.of(this.getCurrentUser(getCurrentUserEmail()));
    }

    public UserInfoDto changeUserInfo(UserUpdateRequest dto, HttpServletResponse response) {
        Optional<User> findEmail = userRepository.findByEmailAndUsername(dto.getEmail(), dto.getUsername());
        if (findEmail.isPresent()) {
            throw new AlreadyExistedUserException("Email or Name");
        }
        User currentUser = this.getCurrentUser(getCurrentUserEmail());
        currentUser.changeInfo(
                dto.getUsername(), dto.getEmail()
                , dto.getPassword(), passwordEncoder
                , dto.getBio(), dto.getImage()
        );

        if (!currentUser.getUsername().equals(dto.getUsername())) {
            this.createNewAuthentication(currentUser, response);
        }

        return UserInfoDto.of(currentUser);
    }

    private void confirmPassword(UserLoginDto dto, User user) {
        boolean matched = user.isMatched(dto.getPassword(), user.getPassword(), passwordEncoder);
        if (!matched) {
            throw new PasswordNotMatchedException(user.getEmail());
        }
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email + " not found."));
    }

    private void createNewAuthentication(User user, HttpServletResponse response) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList()));
        this.createToken(user, response);
    }

    private void createToken(User user, HttpServletResponse response) {
        final String token = jwtProvider.createToken(user.getEmail());
        this.saveTokenInCookie(token, response);
    }

    private void saveTokenInCookie(String token, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(header, token)
                .maxAge(validitySeconds)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("Lax")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, String token) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for(Cookie cookie : cookies) {
                if (header.equals(cookie.getName())) {
                    ResponseCookie responseCookie = ResponseCookie.from(header, token)
                            .maxAge(0)
                            .path("/")
                            .secure(true)
                            .httpOnly(true)
                            .sameSite("Lax")
                            .build();

                    response.setHeader("Set-Cookie", responseCookie.toString());
                }
            }
        }
    }

}
