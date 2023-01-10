package ifi.realworld.user.app.service;

import ifi.realworld.common.security.CustomUserDetailsService;
import ifi.realworld.user.api.UserPasswordEncoder;
import ifi.realworld.user.domain.FollowRelation;
import ifi.realworld.user.domain.repository.FollowRepository;
import ifi.realworld.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
    @Autowired
    EntityManager em;

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

    @DisplayName("팔로우")
    @Test
    public void followUser() {
        //given
        Long followerId = 1L;
        Long followeeId = 2L;
        FollowRelation followRelation = new FollowRelation(followerId, followeeId);

        //when
        FollowRelation save = followRepository.save(followRelation);

        //then
        assertThat(save.getFollowRelationId().getFollowerId()).isEqualTo(followerId);
        assertThat(save.getFollowRelationId().getFolloweeId()).isEqualTo(followeeId);
    }

    @DisplayName("언팔로우")
    @Test
    public void unFollowUser() {
        //given
        long followerId = 1L;
        long followeeId = 2L;
        setFollowRelation(followerId, followeeId);
        em.flush();
        em.clear();

        //when
        followRepository.deleteByFollowRelationIdFollowerIdAndFollowRelationIdFolloweeId(followerId, followeeId);

        //then
        boolean followed = followRepository.existsByFollowRelationIdFollowerIdAndFollowRelationIdFolloweeId(followerId, followeeId);
        assertThat(followed).isFalse();
    }

    private void setFollowRelation(long followerId, long followeeId) {
        FollowRelation followRelation = new FollowRelation(followerId, followeeId);
        FollowRelation save = followRepository.save(followRelation);
    }

}