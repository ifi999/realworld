package ifi.realworld.user.domain.repository;

import ifi.realworld.user.domain.FollowRelation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private FollowJpaRepository followJpaRepository;

    @Autowired
    private FollowRepository followRepository;

    @BeforeEach
    void setUp() {
        long followerId = 1l;
        long followeeId = 2l;

        FollowRelation followRelation = new FollowRelation(followerId, followeeId);
        followRepository.save(followRelation);
    }

    @AfterEach
    void tearDown() {
        followRepository.deleteAllInBatch();
    }

    @DisplayName("상대 유저가 팔로우되어 있는지 확인한다.")
    @Test
    public void isFollow() {
        // given
        long followerId = 1l;
        long followeeId = 2l;

        // when
        boolean isFollow = followJpaRepository.isFollow(followerId, followeeId);

        // then
        assertThat(isFollow).isTrue();
    }

    @DisplayName("상대 유저를 대상으로 한 팔로우를 취소한다.")
    @Test
    public void unFollow() {
        // given
        long followerId = 1l;
        long followeeId = 2l;

        // when
        followJpaRepository.unFollow(followerId, followeeId);

        // then
        boolean isFollow = followJpaRepository.isFollow(followerId, followeeId);
        assertThat(isFollow).isFalse();
    }

}