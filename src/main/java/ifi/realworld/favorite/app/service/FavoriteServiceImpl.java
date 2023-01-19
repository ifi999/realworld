package ifi.realworld.favorite.app.service;

import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.ArticleTag;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.article.domain.repository.ArticleTagJpaRepository;
import ifi.realworld.common.exception.AlreadyRegistArticleFavorite;
import ifi.realworld.common.exception.ArticleNotFoundException;
import ifi.realworld.common.exception.NotFoundArticleFavoriteRelation;
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

    // TODO - 여기 과정이 너무 난잡한듯............. Entity 구상부터 틀려먹은 느낌

    @Override
    public SingleArticleDto favoriteArticle(String email, String slug) {
        User currentUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);

        Boolean favorited = favoriteJpaRepository.isFavorited(article.getId(), currentUser.getId());
        if (favorited) throw new AlreadyRegistArticleFavorite("This " + article.getTitle() + " has already been favorited.");

        Favorite favorite = Favorite.builder()
                .articleId(article)
                .userId(currentUser)
                .build();
        Favorite saved = favoriteRepository.save(favorite);
        long favoriteCount = favoriteJpaRepository.articleFavoriteCount(article.getId());

        return SingleArticleDto.builder()
                .article(saved.getArticle())
                .author(saved.getArticle().getAuthor())
                .tagList(getTags(article.getId()))
                .favorited(true)
                .favoritesCount(favoriteCount)
                .build();
    }

    @Override
    public SingleArticleDto unfavoriteArticle(String email, String slug) {
        User currentUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);

        Boolean favorited = favoriteJpaRepository.isFavorited(article.getId(), currentUser.getId());
        if (!favorited) throw new NotFoundArticleFavoriteRelation();

        Favorite favorite = favoriteJpaRepository.findByArticleAndUser(article.getId(), currentUser.getId());
        favoriteRepository.delete(favorite);
        long favoriteCount = favoriteJpaRepository.articleFavoriteCount(article.getId());

        return SingleArticleDto.builder()
                .article(article)
                .author(article.getAuthor())
                .tagList(getTags(article.getId()))
                .favorited(false)
                .favoritesCount(favoriteCount)
                .build();
    }

    private List<Tag> getTags(Long articldId){
        List<ArticleTag> articleTags = articleTagJpaRepository.findByArticleId(articldId);
        return articleTags.stream()
                .map(o -> o.getTag())
                .collect(Collectors.toList());
    }
}
