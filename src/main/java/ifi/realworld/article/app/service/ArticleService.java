package ifi.realworld.article.app.service;

import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.MultipleArticleDto;
import ifi.realworld.article.api.dto.SingleArticleDto;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    SingleArticleDto createArticles(ArticleCreateRequest dto);

    MultipleArticleDto getArticles(ArticleSearchDto dto, Pageable pageable);
}
