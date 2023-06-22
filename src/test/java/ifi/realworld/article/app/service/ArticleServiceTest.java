package ifi.realworld.article.app.service;

import ifi.realworld.TestSupport;
import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.ArticleUpdateRequest;
import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.domain.Article;
import ifi.realworld.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class ArticleServiceTest extends TestSupport {

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test email")
                .username("테스트")
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .build();
        userRepository.save(user);
    }

    @DisplayName("게시글을 작성한다.")
    @Test
    public void createArticles() {
        // given
        setUserDetailService("test email");

        ArticleCreateRequest request = new ArticleCreateRequest();
        ReflectionTestUtils.setField(request, "title", "test title");
        ReflectionTestUtils.setField(request, "description", "test description");
        ReflectionTestUtils.setField(request, "body", "test body");

        // when
        SingleArticleDto article = articleService.createArticles(request);

        // then
        assertThat(article).isNotNull();
        assertThat(article.getTitle()).isEqualTo(request.getTitle());
        assertThat(article.getDescription()).isEqualTo(request.getDescription());
        assertThat(article.getBody()).isEqualTo(request.getBody());
    }

    @DisplayName("게시글 목록을 조회한다.")
    @Test
    public void getArticles() {
        // given
        setUserDetailService("test email");

        int articleCnt = 15;
        for (int i = 0; i < articleCnt; i++) {
            setArticle("test title" + i, "test body" + i, "test desc" + i);
        }

        ArticleSearchDto request = new ArticleSearchDto();
        ReflectionTestUtils.setField(request, "author", "test email");
        Pageable pageable = PageRequest.of(0, 5);
        
        // when
        Page<SingleArticleDto> articles = articleService.getArticles(request, pageable);

        // then
        assertThat(articles.getTotalElements()).isEqualTo(articleCnt);
    }

    @DisplayName("게시글을 조회한다.")
    @Test
    public void getArticle() {
        // given
        setUserDetailService("test email");

        Article article = setArticle("tset title", "test body", "test desc");
        Article savedArticle = articleRepository.save(article);
        String slug = savedArticle.getSlug();

        // when
        SingleArticleDto getArticle = articleService.getArticle(slug);

        // then
        assertThat(getArticle).isNotNull();
        assertThat(getArticle.getTitle()).isEqualTo(savedArticle.getTitle());
        assertThat(getArticle.getBody()).isEqualTo(savedArticle.getBody());
        assertThat(getArticle.getDescription()).isEqualTo(savedArticle.getDescription());
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    public void changeArticleInfo() {
        // given
        setUserDetailService("test email");

        Article article = setArticle("tset title", "test body", "test desc");
        Article savedArticle = articleRepository.save(article);
        String slug = savedArticle.getSlug();

        ArticleUpdateRequest request = new ArticleUpdateRequest();
        ReflectionTestUtils.setField(request, "title", "change title");
        ReflectionTestUtils.setField(request, "description", "change desc");
        ReflectionTestUtils.setField(request, "body", "change body");

        // when
        SingleArticleDto changeArticle = articleService.changeArticleInfo(slug, request);

        // then
        assertThat(changeArticle.getTitle()).isEqualTo(request.getTitle());
        assertThat(changeArticle.getBody()).isEqualTo(request.getBody());
        assertThat(changeArticle.getDescription()).isEqualTo(request.getDescription());
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    public void deleteArticle() {
        // given
        setUserDetailService("test email");

        Article article = setArticle("tset title", "test body", "test desc");
        Article savedArticle = articleRepository.save(article);
        String slug = savedArticle.getSlug();

        // when
        articleService.deleteArticle(slug);

        // then
        Optional<Article> getArticle = articleRepository.findBySlug(slug);
        assertThat(getArticle).isEmpty();
    }

    private void setUserDetailService(String email) {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Authentication authenticationMock = Mockito.mock(Authentication.class);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getName()).thenReturn(email);

        SecurityContextHolder.setContext(securityContextMock);
    }

    private Article setArticle(String title, String body, String desc) {
        Article article = Article.builder()
                .title(title)
                .author(user)
                .body(body)
                .description(desc)
                .build();
        return articleRepository.save(article);
    }

}