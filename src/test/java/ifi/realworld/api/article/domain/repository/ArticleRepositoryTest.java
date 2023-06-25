package ifi.realworld.api.article.domain.repository;

import ifi.realworld.api.TestSupport;
import ifi.realworld.article.domain.Article;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class ArticleRepositoryTest extends TestSupport {

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

    @DisplayName("제목으로 게시글을 조회한다.")
    @Test
    public void findBySlug() {
        // given
        Article article = setArticle("test title", "test body", "test desc");
        String slug = article.getSlug();

        // when
        Optional<Article> getArticle = articleRepository.findBySlug(slug);

        // then
        assertThat(getArticle).isPresent();
        assertThat(getArticle.get().getTitle()).isEqualTo(article.getTitle());
        assertThat(getArticle.get().getBody()).isEqualTo(article.getBody());
        assertThat(getArticle.get().getDescription()).isEqualTo(article.getDescription());
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