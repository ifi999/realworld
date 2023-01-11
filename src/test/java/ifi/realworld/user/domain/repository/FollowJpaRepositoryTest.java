package ifi.realworld.user.domain.repository;

import ifi.realworld.user.domain.FollowRelation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class FollowJpaRepositoryTest {

    @Autowired
    FollowJpaRepository followJpaRepository;
    @Autowired
    FollowRepository followRepository;

    @DisplayName("팔로우 여부")
    @Test
    public void isFollow() {
        //given
        long user1 = 1L;
        long user2 = 2L;
        followRepository.save(new FollowRelation(user1, user2));

        //when
        boolean follow = followJpaRepository.isFollow(user1, user2);
        boolean unFollow = followJpaRepository.isFollow(user2, user1);

        //then
        assertThat(follow).isTrue();
        assertThat(unFollow).isFalse();
    }

    @DisplayName("언팔로우")
    @Test
    public void unFollow() {
        //given
        long user1 = 1L;
        long user2 = 2L;
        followRepository.save(new FollowRelation(user1, user2));

        //when
        followJpaRepository.unFollow(user1, user2);
        boolean unfollow = followJpaRepository.isFollow(user1, user2);

        //then
        assertThat(unfollow).isFalse();
    }

}