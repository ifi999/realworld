package ifi.realworld.api.article.domain.repository;

import ifi.realworld.api.TestSupport;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.domain.Article;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleJpaRepositoryTest extends TestSupport {

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

    @DisplayName("게시글 목록을 조회한다.")
    @Test
    public void getArticles() {
        // given
        int articleCnt = 15;
        for (int i = 0; i < articleCnt; i++) {
            setArticle("test title" + i, "test body" + i, "test desc" + i);
        }

        ArticleSearchDto request = new ArticleSearchDto();
        ReflectionTestUtils.setField(request, "author", "test email");
        Pageable pageable = PageRequest.of(0, 5);

        // when
        Page<SingleArticleDto> articles = articleJpaRepository.getArticles(request, pageable);

        // then
        assertThat(articles).isNotNull();
        assertThat(articles.getTotalElements()).isEqualTo(15);
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