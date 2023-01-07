package ifi.realworld.user.api;

import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserCreateResponse;
import ifi.realworld.user.app.service.UserService;
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

    private final UserService userService;

    @PostMapping("/users")
    public UserCreateResponse createUser(@RequestBody @Valid UserCreateRequest dto) {
        User user = userService.createUser(dto);

        return UserCreateResponse.toEntity(user);
    }

}
