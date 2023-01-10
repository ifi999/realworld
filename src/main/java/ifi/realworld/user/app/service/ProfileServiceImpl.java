package ifi.realworld.user.app.service;

import ifi.realworld.common.exception.UserNotFoundException;
import ifi.realworld.common.security.CustomUserDetailsService;
import ifi.realworld.user.domain.FollowRelation;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.FollowRepository;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    public Pair<User, Boolean> getProfile(String username) {
        User currentUser = getCurrentUser(username);
        User findUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username + " not found."));
        boolean followed = followRepository.existsByFollowRelationIdFollowerIdAndFollowRelationIdFolloweeId(currentUser.getId(), findUser.getId());
        return Pair.of(findUser, followed);
    }

    @Override
    public Pair<User, Boolean> followUser(String username) {
        User currentUser = getCurrentUser(username);
        User findUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username + " not found."));
        FollowRelation followRelation = new FollowRelation(currentUser.getId(), findUser.getId());
        followRepository.save(followRelation);
        return Pair.of(findUser, true);
    }

    @Override
    public Pair<User, Boolean> unFollowUser(String username) {
        User currentUser = getCurrentUser(username);
        User findUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username + " not found."));
        followRepository.deleteByFollowRelationIdFollowerIdAndFollowRelationIdFolloweeId(currentUser.getId(), findUser.getId());
        return Pair.of(findUser, false);
    }

    private User getCurrentUser(String username) {
        String currentUserEmail = getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new UserNotFoundException(currentUserEmail + " not found."));
        if (username.equals(currentUser.getUsername())) throw new IllegalArgumentException("Can't access own profile page"); // TODO - 적절한 exception을 모르겠음. 찾아보기
        return currentUser;
    }

    private String getCurrentUserEmail() {
        UserDetails currentUserDetails = CustomUserDetailsService.getCurrentUserDetails();
        return currentUserDetails.getUsername();
    }
}
