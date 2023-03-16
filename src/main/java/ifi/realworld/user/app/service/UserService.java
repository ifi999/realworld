package ifi.realworld.user.app.service;

import ifi.realworld.user.api.dto.*;

import javax.servlet.http.HttpServletResponse;

public interface UserService {

    UserCreateResponse createUser(UserCreateRequest dto);

    UserLoginDto login(UserLoginDto dto, HttpServletResponse response);

    UserInfoDto getCurrentUserInfo();

    UserInfoDto changeUserInfo(UserUpdateRequest dto, HttpServletResponse response);
}
