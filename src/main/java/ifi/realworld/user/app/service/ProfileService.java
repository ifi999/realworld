package ifi.realworld.user.app.service;

import ifi.realworld.user.domain.User;
import org.springframework.data.util.Pair;

public interface ProfileService {

    Pair<User, Boolean> getProfile(String username);

    Pair<User, Boolean> followUser(String username);
}
