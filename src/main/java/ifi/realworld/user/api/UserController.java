package ifi.realworld.user.api;

import ifi.realworld.user.api.dto.*;
import ifi.realworld.user.app.service.UserServiceImpl;
import ifi.realworld.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Transactional
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
    public UserLoginDto login(@RequestBody @Valid final UserLoginDto dto, HttpServletResponse response) {
        return UserLoginDto.of(userService.login(dto, response));
    }

    @GetMapping("/user")
    public UserInfoDto getCurrentUser() {
        // TODO - autoincrement 값 노출안되게 처리하고싶은데
        return UserInfoDto.of(userService.getCurrentUserInfo());
    }

    @PutMapping("/user")
    public UserInfoDto updateUser(@RequestBody @Valid final UserUpdateRequest dto, HttpServletResponse response) {
        User user = userService.updateUser(dto, response);
        return UserInfoDto.of(user);
    }

}
