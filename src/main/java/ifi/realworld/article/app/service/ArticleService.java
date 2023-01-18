package ifi.realworld.article.app.service;

import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.ArticleUpdateRequest;
import ifi.realworld.article.api.dto.SingleArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    SingleArticleDto createArticles(ArticleCreateRequest dto);

    Page<SingleArticleDto> getArticles(ArticleSearchDto dto, Pageable pageable);

    SingleArticleDto updateArticle(String slug, ArticleUpdateRequest dto);

    void deleteArticle(String slug);
}
