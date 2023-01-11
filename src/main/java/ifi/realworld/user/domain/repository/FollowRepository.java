package ifi.realworld.user.domain.repository;

import ifi.realworld.user.domain.FollowRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowRelation, Long> {

}
