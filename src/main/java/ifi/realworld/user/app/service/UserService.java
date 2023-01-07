package ifi.realworld.user.app.service;

import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserLoginDto;
import ifi.realworld.user.domain.User;

public interface UserService {

    User createUser(UserCreateRequest dto);

    User login(UserLoginDto dto);
}
