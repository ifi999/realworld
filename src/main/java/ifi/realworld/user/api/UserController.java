package ifi.realworld.user.api;

import ifi.realworld.user.api.dto.*;
import ifi.realworld.user.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody @Valid final UserCreateRequest dto) {
        UserCreateResponse response = userService.createUser(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserLoginDto> login(@RequestBody @Valid final UserLoginDto dto, HttpServletResponse response) {
        UserLoginDto responseDto = userService.login(dto, response);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/users")
    public ResponseEntity<UserInfoDto> getCurrentUser() {
        UserInfoDto response = userService.getCurrentUserInfo();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users")
    public ResponseEntity<UserInfoDto> changeUserInfo(
            @RequestBody @Valid final UserUpdateRequest dto
            , HttpServletResponse response) {
        UserInfoDto responseDto = userService.changeUserInfo(dto, response);
        return ResponseEntity.ok(responseDto);
    }

}
