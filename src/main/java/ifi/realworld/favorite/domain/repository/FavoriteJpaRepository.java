package ifi.realworld.favorite.domain.repository;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ifi.realworld.favorite.domain.Favorite;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static ifi.realworld.favorite.domain.QFavorite.favorite;

@Repository
public class FavoriteJpaRepository {

    private final JPAQueryFactory queryFactory;

    public FavoriteJpaRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public long articleFavoriteCount(Long articleId) {
        return queryFactory
                .select(Wildcard.count)
                .from(favorite)
                .where(
                        favorite.article.id.eq(articleId)
                )
                .fetchOne();
    }

    public Boolean isFavorited(Long articleId, Long userId) {
        Favorite favorited = queryFactory
                .selectFrom(favorite)
                .where(
                        favorite.article.id.eq(articleId),
                        favorite.user.id.eq(userId)
                )
                .fetchOne();
        return favorited == null ? false : true;
    }

}
