package ifi.realworld.article.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.QSingleArticleDto;
import ifi.realworld.article.api.dto.SingleArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static ifi.realworld.article.domain.QArticle.article;
import static ifi.realworld.article.domain.QArticleTag.articleTag;
import static ifi.realworld.tag.domain.QTag.tag;
import static ifi.realworld.user.domain.QUser.user;

@Repository
public class ArticleJpaRepository {

    private final JPAQueryFactory queryFactory;

    public ArticleJpaRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<SingleArticleDto> getArticles(ArticleSearchDto search, Pageable pageable) {
        List<SingleArticleDto> content = queryFactory
                .select(new QSingleArticleDto(
                        article.id,
                        article.slug,
                        article.title,
                        article.description,
                        article.body,
//                        Expressions.asSimple(JPAExpressions.selectFrom(tag).where(articleTag.tag.eq(tag)).fetch()),
//                        JPAExpressions.selectFrom(tag)
//                                .where(articleTag.tag.eq(tag))
//                                .fetch(),
                        article.author,
                        article.createdAt,
                        article.lastModifiedAt
                ))
                .from(article)
                .innerJoin(article.author, user)
                .leftJoin(article.tagList, articleTag)
                .leftJoin(articleTag.tag, tag)
                .where(
                        tagEq(search.getTag()),
                        authorEq(search.getAuthor())
                )
                .groupBy(article.id)
                .orderBy(
                        article.id.desc()
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long total = queryFactory
                .select(Wildcard.count)
                .from(article)
                .innerJoin(article.author, user)
                .leftJoin(article.tagList, articleTag)
                .leftJoin(articleTag.tag, tag)
                .where(
                        tagEq(search.getTag()),
                        authorEq(search.getAuthor())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression authorEq(String authorName) {
        return StringUtils.hasText(authorName) ? article.author.username.eq(authorName) : null;
    }

    private BooleanExpression tagEq(String tagName) {
        return StringUtils.hasText(tagName) ? tag.name.eq(tagName) : null;
    }

}
