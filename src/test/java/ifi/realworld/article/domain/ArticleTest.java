package ifi.realworld.article.domain;

import ifi.realworld.user.domain.User;
import ifi.realworld.utils.security.CustomUserPasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {
    
    @DisplayName("제목(slug)을 생성한다.")
    @Test
    public void setSlug() {
        // given
        User user = User.builder()
                .email("test email")
                .username("테스트")
                .password("1234")
                .passwordEncoder(new CustomUserPasswordEncoder(new BCryptPasswordEncoder()))
                .build();

        Article article = Article.builder()
                .title("test title")
                .author(user)
                .body("test body")
                .description("test desc")
                .build();
        
        // when
        String slug = article.setSlug();

        // then
        assertThat(article.getSlug()).isNotEqualTo(slug);
        assertThat(article.getSlug()).contains("test-title_");
    }

}