package ifi.realworld.user.app.service;

import ifi.realworld.user.api.dto.ProfileDto;

public interface ProfileService {

    ProfileDto getProfile(String username);

    ProfileDto followUser(String username);

    ProfileDto unFollowUser(String username);
}
