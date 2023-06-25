package ifi.realworld.api.comment.api;

import ifi.realworld.api.TestSupport;
import ifi.realworld.article.domain.Article;
import ifi.realworld.comment.api.dto.CommentCreateRequest;
import ifi.realworld.comment.domain.Comment;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest extends TestSupport {

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

    @WithMockUser
    @DisplayName("게시글에 코멘트를 적는다.")
    @Test
    public void createComments() throws Exception {
        // given
        CommentCreateRequest request = new CommentCreateRequest();
        ReflectionTestUtils.setField(request, "body", "test comment");

        String slug = article.getSlug();

        // then
        mockMvc.perform(post("/api/articles/{slug}/comments", slug)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.body").value("test comment"));
    }

    @DisplayName("게시글의 코멘트 목록을 조회한다.")
    @Test
    public void getComments() throws Exception {
        // given
        String slug = article.getSlug();

        Comment comment1 = new Comment("test comment 1", article);
        Comment comment2 = new Comment("test comment 2", article);
        Comment comment3 = new Comment("test comment 3", article);
        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        // then
        mockMvc.perform(get("/api/articles/{slug}/comments", slug)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].body").exists())
        .andExpect(jsonPath("$[0].body").value("test comment 1"))
        .andExpect(jsonPath("$[1].body").exists())
        .andExpect(jsonPath("$[1].body").value("test comment 2"))
        .andExpect(jsonPath("$[2].body").exists())
        .andExpect(jsonPath("$[2].body").value("test comment 3"));
    }

    @WithMockUser
    @DisplayName("게시글의 코멘트를 삭제한다.")
    @Test
    public void deleteComments() throws Exception {
        // given
        String slug = article.getSlug();
        Long id = article.getId();

        // then
        mockMvc.perform(delete("/api/articles/{slug}/comments/{id}", slug, id)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());

    }

}