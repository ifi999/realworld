package ifi.realworld.user.app.service;

import ifi.realworld.utils.exception.api.UserNotFoundException;
import ifi.realworld.user.api.dto.ProfileDto;
import ifi.realworld.user.domain.FollowRelation;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.FollowJpaRepository;
import ifi.realworld.user.domain.repository.FollowRepository;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final FollowJpaRepository followJpaRepository;

    public ProfileDto getProfile(String username) {
        User currentUser = getCurrentUser(getCurrentUserEmail());
        User findUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username + " not found."));
        boolean followed = followJpaRepository.isFollow(currentUser.getId(), findUser.getId());
        return ProfileDto.of(findUser, followed);
    }

    public ProfileDto followUser(String username) {
        User currentUser = getCurrentUser(getCurrentUserEmail());
        User findUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username + " not found."));
        FollowRelation followRelation = new FollowRelation(currentUser.getId(), findUser.getId());
        followRepository.save(followRelation);
        return ProfileDto.of(findUser, true);
    }

    public ProfileDto unFollowUser(String username) {
        User currentUser = getCurrentUser(getCurrentUserEmail());
        User findUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username + " not found."));
        followJpaRepository.unFollow(currentUser.getId(), findUser.getId());
        return ProfileDto.of(findUser, false);
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email + " not found."));
    }

}
