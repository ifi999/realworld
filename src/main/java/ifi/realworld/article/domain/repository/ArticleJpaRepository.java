package ifi.realworld.article.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ifi.realworld.article.api.dto.MultipleArticleDto;
import ifi.realworld.article.api.dto.QSingleArticleDto;
import ifi.realworld.article.api.dto.SingleArticleDto;
import org.springframework.stereotype.Repository;

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

    public MultipleArticleDto getArticles() {
        List<SingleArticleDto> singleArticleList = queryFactory
                .select(new QSingleArticleDto(
                        article.slug,
                        article.title,
                        article.description,
                        article.body,
                        article.tagList,
                        article.author,
                        article.createdAt,
                        article.lastModifiedAt
                ))
                .from(article)
                .innerJoin(article.author, user)
                .leftJoin(article.tagList, articleTag)
                .leftJoin(articleTag.tag, tag)
                .fetch();

        return new MultipleArticleDto(singleArticleList, singleArticleList.size());
    }

}
