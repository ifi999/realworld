package ifi.realworld.api.article.domain.repository;

import ifi.realworld.api.TestSupport;
import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.ArticleTag;
import ifi.realworld.tag.domain.Tag;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ArticleTagJpaRepositoryTest extends TestSupport {

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

    @DisplayName("게시글 번호로 해당 게시글 태그 목록을 조회한다.")
    @Test
    public void findByArticleId() {
        // given
        Article article = setArticle("test title", "test body", "test desc");
        Long articleId = article.getId();

        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Tag tag3 = new Tag("tag 3");
        tagRepository.saveAll(List.of(tag1, tag2, tag3));

        articleTagRepository.save(new ArticleTag(article, tag1));
        articleTagRepository.save(new ArticleTag(article, tag2));
        articleTagRepository.save(new ArticleTag(article, tag3));

        // when
        List<ArticleTag> articleTags = articleTagJpaRepository.findByArticleId(articleId);

        // then
        assertThat(articleTags).hasSize(3);
        assertThat(articleTags)
                .extracting("article", "tag")
                .contains(
                        tuple(article, tag1),
                        tuple(article, tag2),
                        tuple(article, tag3)
                );
        assertThat(articleTags.get(0).getTag().getName()).isEqualTo(tag1.getName());
        assertThat(articleTags.get(1).getTag().getName()).isEqualTo(tag2.getName());
        assertThat(articleTags.get(2).getTag().getName()).isEqualTo(tag3.getName());
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