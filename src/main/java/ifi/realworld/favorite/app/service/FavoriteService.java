package ifi.realworld.favorite.app.service;

import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.ArticleTag;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.article.domain.repository.ArticleTagJpaRepository;
import ifi.realworld.comment.domain.Comment;
import ifi.realworld.comment.domain.repository.CommentRepository;
import ifi.realworld.utils.exception.api.AlreadyRegistArticleFavoriteException;
import ifi.realworld.utils.exception.api.ArticleNotFoundException;
import ifi.realworld.utils.exception.api.NotFoundArticleFavoriteRelationException;
import ifi.realworld.utils.exception.api.UserNotFoundException;
import ifi.realworld.favorite.domain.Favorite;
import ifi.realworld.favorite.domain.repository.FavoriteJpaRepository;
import ifi.realworld.favorite.domain.repository.FavoriteRepository;
import ifi.realworld.tag.domain.Tag;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteJpaRepository favoriteJpaRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleTagJpaRepository articleTagJpaRepository;
    private final CommentRepository commentRepository;

    public SingleArticleDto favoriteArticle(String slug) {
        User currentUser = userRepository.findByEmail(getCurrentUserEmail()).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
        List<Comment> commentList = getCommentList(article);

        Boolean favorited = favoriteJpaRepository.isFavorited(article.getId(), currentUser.getId());
        if (favorited) throw new AlreadyRegistArticleFavoriteException("This " + article.getTitle() + " has already been favorited.");

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
                .commentList(commentList)
                .favorited(true)
                .favoritesCount(favoriteCount)
                .build();
    }

    public SingleArticleDto unfavoriteArticle(String slug) {
        User currentUser = userRepository.findByEmail(getCurrentUserEmail()).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
        List<Comment> commentList = getCommentList(article);

        Boolean favorited = favoriteJpaRepository.isFavorited(article.getId(), currentUser.getId());
        if (!favorited) throw new NotFoundArticleFavoriteRelationException();

        Favorite favorite = favoriteJpaRepository.findByArticleAndUser(article.getId(), currentUser.getId());
        favoriteRepository.delete(favorite);
        long favoriteCount = favoriteJpaRepository.articleFavoriteCount(article.getId());

        return SingleArticleDto.builder()
                .article(article)
                .author(article.getAuthor())
                .tagList(getTags(article.getId()))
                .commentList(commentList)
                .favorited(false)
                .favoritesCount(favoriteCount)
                .build();
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private List<Comment> getCommentList(Article article) {
        return commentRepository.findByArticleId(article.getId());
    }

    private List<Tag> getTags(Long articldId){
        List<ArticleTag> articleTags = articleTagJpaRepository.findByArticleId(articldId);
        return articleTags.stream()
                .map(o -> o.getTag())
                .collect(Collectors.toList());
    }
}
