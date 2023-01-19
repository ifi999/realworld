package ifi.realworld.favorite.app.service;

import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.ArticleTag;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.article.domain.repository.ArticleTagJpaRepository;
import ifi.realworld.common.exception.AlreadyRegistArticleFavorite;
import ifi.realworld.common.exception.ArticleNotFoundException;
import ifi.realworld.common.exception.UserNotFoundException;
import ifi.realworld.favorite.domain.Favorite;
import ifi.realworld.favorite.domain.repository.FavoriteJpaRepository;
import ifi.realworld.favorite.domain.repository.FavoriteRepository;
import ifi.realworld.tag.domain.Tag;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteJpaRepository favoriteJpaRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleTagJpaRepository articleTagJpaRepository;

    @Override
    public SingleArticleDto favoriteArticle(String email, String slug) {
        // TODO - 너무 복잡해보임
        User currentUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
        List<ArticleTag> articleTags = articleTagJpaRepository.findByArticleId(article.getId());
        List<Tag> tags = articleTags.stream()
                .map(o -> o.getTag())
                .collect(Collectors.toList());
        Favorite favorite = Favorite.builder()
                .articleId(article)
                .userId(currentUser)
                .build();

        Boolean favorited = favoriteJpaRepository.isFavorited(article.getId(), currentUser.getId());
        if (favorited) throw new AlreadyRegistArticleFavorite("This " + article.getTitle() + " has already been favorited.");

        Favorite saved = favoriteRepository.save(favorite);
        long favoriteCount = favoriteJpaRepository.articleFavoriteCount(article.getId());

        return SingleArticleDto.builder()
                .article(saved.getArticle())
                .author(saved.getArticle().getAuthor())
                .tagList(tags)
                .favorited(true)
                .favoritesCount(favoriteCount)
                .build();
    }
}
