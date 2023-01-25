package ifi.realworld.user.app.service;

import ifi.realworld.user.api.dto.ProfileDto;
import org.springframework.security.core.userdetails.User;

public interface ProfileService {

    ProfileDto getProfile(String username, User user);

    ProfileDto followUser(String username, User user);

    ProfileDto unFollowUser(String username, User user);
}
