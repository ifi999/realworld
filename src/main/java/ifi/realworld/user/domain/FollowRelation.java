package ifi.realworld.user.domain;

import ifi.realworld.utils.entity.BaseCreateInfoEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowRelation extends BaseCreateInfoEntity {

    @EmbeddedId
    private FollowRelationId followRelationId;

    public FollowRelation(FollowRelationId followRelationId) {
        this.followRelationId = followRelationId;
    }

    public FollowRelation(long followerId, long followeeId) {
        this.followRelationId = new FollowRelationId(followerId, followeeId);
    }
}
