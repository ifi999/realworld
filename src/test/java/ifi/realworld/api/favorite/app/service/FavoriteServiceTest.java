package ifi.realworld.api.favorite.app.service;

import ifi.realworld.api.TestSupport;
import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.domain.Article;
import ifi.realworld.user.domain.User;
import ifi.realworld.utils.exception.api.AlreadyRegistArticleFavoriteException;
import ifi.realworld.utils.exception.api.ArticleNotFoundException;
import ifi.realworld.utils.exception.api.NotFoundArticleFavoriteRelationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteServiceTest extends TestSupport {

    private Article article;

    @BeforeEach
    void setUp() {
        User user = User.builder()
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

        setUserDetailService("test email");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("게시글에 좋아요를 한다.")
    @Test
    public void favoriteArticle() {
        // given
        String slug = article.getSlug();

        // when
        SingleArticleDto singleArticleDto = favoriteService.favoriteArticle(slug);

        // then
        assertThat(singleArticleDto.isFavorited()).isTrue();
    }

    @DisplayName("존재하지 않는 게시글은 좋아요를 할 수 없다.")
    @Test
    public void favoriteArticleWithNotFoundArticle() {
        // given
        String slug = "not found article slug";

        // then
        assertThatThrownBy(() -> favoriteService.favoriteArticle(slug))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage("Not found this article.");
    }

    @DisplayName("이미 게시글이 좋아요가 되어있다면 실패한다.")
    @Test
    public void favoriteArticleWithAlreadyFavorited() {
        // given
        String slug = article.getSlug();

        favoriteService.favoriteArticle(slug);

        // then
        assertThatThrownBy(() -> favoriteService.favoriteArticle(slug))
                .isInstanceOf(AlreadyRegistArticleFavoriteException.class)
                .hasMessage("This " + article.getTitle() + " has already been favorited.");
    }

    @DisplayName("게시글에 좋아요한 것을 취소한다.")
    @Test
    public void unfavoriteArticle() {
        // given
        String slug = article.getSlug();
        favoriteService.favoriteArticle(slug);

        // when
        SingleArticleDto singleArticleDto = favoriteService.unfavoriteArticle(slug);

        // then
        assertThat(singleArticleDto.isFavorited()).isFalse();
    }

    @DisplayName("존재하지 않는 게시글은 좋아요 취소를 할 수 없다.")
    @Test
    public void unfavoriteArticleWithNotFoundArticle() {
        // given
        String slug = "not found article slug";

        // then
        assertThatThrownBy(() -> favoriteService.unfavoriteArticle(slug))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage("Not found this article.");
    }

    @DisplayName("좋아요가 되지 않은 게시글은 좋아요 취소를 할 수 없다.")
    @Test
    public void unfavoriteArticleWithNotFoundArticleFavoriteRelation() {
        // given
        String slug = article.getSlug();

        // then
        assertThatThrownBy(() -> favoriteService.unfavoriteArticle(slug))
                .isInstanceOf(NotFoundArticleFavoriteRelationException.class)
                .hasMessage("Not found this article's favorite relation.");
    }

    private void setUserDetailService(String email) {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Authentication authenticationMock = Mockito.mock(Authentication.class);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getName()).thenReturn(email);

        SecurityContextHolder.setContext(securityContextMock);
    }

}