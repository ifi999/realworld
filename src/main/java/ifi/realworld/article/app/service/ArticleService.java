package ifi.realworld.article.app.service;

import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.ArticleUpdateRequest;
import ifi.realworld.article.api.dto.SingleArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;

public interface ArticleService {

    SingleArticleDto createArticles(ArticleCreateRequest dto, User user);

    Page<SingleArticleDto> getArticles(ArticleSearchDto dto, Pageable pageable);

    SingleArticleDto updateArticle(String slug, ArticleUpdateRequest dto, User user);

    void deleteArticle(String slug);

    SingleArticleDto getArticle(String slug, User user);
}
