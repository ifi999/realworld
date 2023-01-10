package ifi.realworld.user.domain.repository;

import ifi.realworld.user.domain.FollowRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowRelation, Long> {

    // TODO - queryDsl 같은 것으로 명칭 줄이기
    boolean existsByFollowRelationIdFollowerIdAndFollowRelationIdFolloweeId(long followerId, long followeeId);

    void deleteByFollowRelationIdFollowerIdAndFollowRelationIdFolloweeId(long followerId, long followeeId);

}
