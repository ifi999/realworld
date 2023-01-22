package ifi.realworld.user.app.service;

import ifi.realworld.user.api.dto.*;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletResponse;

public interface UserService {

    UserCreateResponse createUser(UserCreateRequest dto);

    UserLoginDto login(UserLoginDto dto, HttpServletResponse response);

    UserInfoDto getCurrentUserInfo(User user);

    UserInfoDto updateUser(UserUpdateRequest dto, HttpServletResponse response, User user);
}
