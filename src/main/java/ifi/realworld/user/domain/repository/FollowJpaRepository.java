package ifi.realworld.user.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static ifi.realworld.user.domain.QFollowRelation.followRelation;

@Repository
public class FollowJpaRepository {

    private final JPAQueryFactory queryFactory;

    public FollowJpaRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public boolean isFollow(final long followerId, final long followeeId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(followRelation)
                .where(
                        followerIdAndFolloweeIdEq(followerId, followeeId)
                )
                .fetchFirst();
        return fetchOne != null;
    }

    public void unFollow(final long followerId, final long followeeId) {
        queryFactory
                .delete(followRelation)
                .where(
                        followerIdAndFolloweeIdEq(followerId, followeeId)
                )
                .execute();
    }

    private static BooleanExpression followerIdAndFolloweeIdEq(long followerId, long followeeId) {
        return followRelation.followRelationId.followerId.eq(followerId)
                .and(followRelation.followRelationId.followeeId.eq(followeeId));
    }

}
