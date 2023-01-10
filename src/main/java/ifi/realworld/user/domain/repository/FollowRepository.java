package ifi.realworld.user.domain.repository;

import ifi.realworld.user.domain.FollowRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowRelation, Long> {

    boolean existsByFollowRelationIdFollowerIdAndFollowRelationIdFolloweeId(long followerId, long followeeId);

}
