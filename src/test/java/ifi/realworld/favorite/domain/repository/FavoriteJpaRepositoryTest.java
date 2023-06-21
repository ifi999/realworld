package ifi.realworld.favorite.domain.repository;

import ifi.realworld.TestSupport;
import ifi.realworld.article.domain.Article;
import ifi.realworld.favorite.domain.Favorite;
import ifi.realworld.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteJpaRepositoryTest extends TestSupport {

    User user;

    Article article;

    Favorite favorite;

    @DisplayName("게시글의 좋아요 수를 구한다.")
    @Test
    public void articleFavoriteCount() {
        // given
        setArticle();

        // when
        long count = favoriteJpaRepository.articleFavoriteCount(article.getId());

        // then
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("게시글의 좋아요가 없다면 0을 반환한다.")
    @Test
    public void articleFavoriteCountZero() {
        // given
        setArticle();
        favoriteRepository.delete(favorite);

        // when
        long count = favoriteJpaRepository.articleFavoriteCount(article.getId());

        // then
        assertThat(count).isEqualTo(0);
    }

    @DisplayName("좋아요 여부를 확인한다.")
    @Test
    public void isFavorited() {
        // given
        setArticle();

        // when
        boolean favoritedTrue = favoriteJpaRepository.isFavorited(article.getId(), user.getId());

        favoriteRepository.delete(favorite);
        boolean favoritedFalse = favoriteJpaRepository.isFavorited(article.getId(), user.getId());

        // then
        assertThat(favoritedTrue).isTrue();
        assertThat(favoritedFalse).isFalse();
    }

    private void setArticle() {
        user = User.builder()
                .email("test email")
                .username("테스트")
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .build();
        userRepository.save(user);

        article = Article.builder()
                .title("test title")
                .author(user)
                .body("test body")
                .description("test desc")
                .build();
        articleRepository.save(article);

        favorite = Favorite.builder()
                .articleId(article)
                .userId(user)
                .build();
        favoriteRepository.save(favorite);
    }

}