package ifi.realworld.article.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ifi.realworld.article.domain.ArticleTag;
import ifi.realworld.article.domain.QArticleTag;
import ifi.realworld.tag.domain.QTag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static ifi.realworld.article.domain.QArticleTag.articleTag;
import static ifi.realworld.tag.domain.QTag.tag;

@Repository
public class ArticleTagJpaRepository {

    private final JPAQueryFactory queryFactory;

    public ArticleTagJpaRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ArticleTag> findByArticleId(Long id) {
        return queryFactory
                .selectFrom(articleTag)
                .leftJoin(articleTag.tag, tag).fetchJoin()
                .where(
                        articleTag.article.id.eq(id)
                )
                .fetch();
    }

}
