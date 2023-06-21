package ifi.realworld.favorite.api;

import ifi.realworld.TestSupport;
import ifi.realworld.article.domain.Article;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavoriteControllerTest extends TestSupport {

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
    }

    @WithMockUser(username = "test email", roles = "USER", password = "1234")
    @DisplayName("게시글에 좋아요를 한다.")
    @Test
    public void favoriteArticle() throws Exception {
        // given
        String slug = article.getSlug();

        // then
        mockMvc.perform(
                post("/api/articles/{slug}/favorite", slug)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value(article.getTitle()))
        .andExpect(jsonPath("$.author.username").value(article.getAuthor().getUsername()))
        .andExpect(jsonPath("$.body").value(article.getBody()))
        .andExpect(jsonPath("$.description").value(article.getDescription()));
    }

    @WithMockUser(username = "test email", roles = "USER", password = "1234")
    @DisplayName("게시글에 좋아요한 것을 취소한다.")
    @Test
    public void unFavoriteArticle() throws Exception {
        // given
        String slug = article.getSlug();

        // then
        mockMvc.perform(delete("/api/articles/{slug}/favorite", slug)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }

}