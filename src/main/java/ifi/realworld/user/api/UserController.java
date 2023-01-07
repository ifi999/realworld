package ifi.realworld.user.api;

import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserCreateResponse;
import ifi.realworld.user.api.dto.UserLoginDto;
import ifi.realworld.user.app.service.UserServiceImpl;
import ifi.realworld.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/users")
    public UserCreateResponse createUser(@RequestBody @Valid final UserCreateRequest dto) {
        return UserCreateResponse.of(userService.createUser(dto));
    }

    @PostMapping("/users/login")
    public UserLoginDto login(@RequestBody @Valid final UserLoginDto dto) {
        return UserLoginDto.of(userService.login(dto));
    }

}
