package ifi.realworld.article.app.service;

import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.SingleArticleDto;

public interface ArticleService {

    SingleArticleDto createArticles(ArticleCreateRequest dto);

}
