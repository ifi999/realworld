package ifi.realworld.favorite.app.service;

import ifi.realworld.article.api.dto.SingleArticleDto;

public interface FavoriteService {

    SingleArticleDto favoriteArticle(String slug);

    SingleArticleDto unfavoriteArticle(String slug);
}
