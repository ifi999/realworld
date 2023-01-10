package ifi.realworld.user.app.service;

import ifi.realworld.common.security.CustomUserDetailsService;
import ifi.realworld.user.api.UserPasswordEncoder;
import ifi.realworld.user.domain.repository.FollowRepository;
import ifi.realworld.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ProfileServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    UserPasswordEncoder passwordEncoder;

    @DisplayName("프로필 조회")
    @Test
    public void getProfile() {
        //given
        Long followerId = 1L;
        Long followeeId = 2L;

        //when
        boolean followed = followRepository.existsByFollowRelationIdFollowerIdAndFollowRelationIdFolloweeId(followerId, followeeId);

        //then
        assertThat(followed).isFalse();
    }

}