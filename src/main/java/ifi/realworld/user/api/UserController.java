package ifi.realworld.user.api;

import ifi.realworld.user.api.dto.*;
import ifi.realworld.user.app.service.UserService;
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

    private final UserService userService;

    @PostMapping("/users")
    public UserCreateResponse createUser(@RequestBody @Valid final UserCreateRequest dto) {
        return userService.createUser(dto);
    }

    @PostMapping("/users/login")
    public UserLoginDto login(@RequestBody @Valid final UserLoginDto dto, HttpServletResponse response) {
        return userService.login(dto, response);
    }

    @GetMapping("/user")
    public UserInfoDto getCurrentUser() {
        return userService.getCurrentUserInfo();
    }

    @PutMapping("/user")
    public UserInfoDto updateUser(
            @RequestBody @Valid final UserUpdateRequest dto
            , HttpServletResponse response) {
        return userService.updateUser(dto, response);
    }

}
