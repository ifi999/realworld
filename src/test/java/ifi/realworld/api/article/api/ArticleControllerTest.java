package ifi.realworld.api.article.api;

import ifi.realworld.api.TestSupport;
import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.ArticleUpdateRequest;
import ifi.realworld.article.domain.Article;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ArticleControllerTest extends TestSupport {

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

    @WithMockUser(username = "test email")
    @DisplayName("게시글을 작성한다.")
    @Test
    public void createArticle() throws Exception {
        // given
        ArticleCreateRequest request = new ArticleCreateRequest();
        ReflectionTestUtils.setField(request, "title", "test title");
        ReflectionTestUtils.setField(request, "description", "test description");
        ReflectionTestUtils.setField(request, "body", "test body");

        // then
        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.title").value("test title"))
        .andExpect(jsonPath("$.description").value("test description"))
        .andExpect(jsonPath("$.body").value("test body"));
    }

    @DisplayName("게시글 목록을 조회한다.")
    @Test
    public void getArticles() throws Exception {
        // given
        int articleCnt = 15;
        for (int i = 0; i < articleCnt; i++) {
            setArticle("test title" + i, "test body" + i, "test desc" + i);
        }

        ArticleSearchDto request = new ArticleSearchDto();
        ReflectionTestUtils.setField(request, "author", "test email");
        Pageable pageable = PageRequest.of(0, 7);

        // then
        mockMvc.perform(get("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .content(objectMapper.writeValueAsString(pageable))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.content[0].title").value("test title14"))
        .andExpect(jsonPath("$.totalElements").value(articleCnt));
    }

    @WithMockUser(username = "test email")
    @DisplayName("게시글을 조회한다.")
    @Test
    public void getArticle() throws Exception {
        // given
        Article article = Article.builder()
                .title("test title")
                .author(user)
                .body("test body")
                .description("test desc")
                .build();
        Article savedArticle = articleRepository.save(article);
        String slug = savedArticle.getSlug();

        // then
        mockMvc.perform(get("/api/articles/{slug}", slug)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.title").value("test title"))
        .andExpect(jsonPath("$.body").value("test body"))
        .andExpect(jsonPath("$.description").value("test desc"));
    }

    @WithMockUser(username = "test email")
    @DisplayName("게시글을 수정한다.")
    @Test
    public void changeArticleInfo() throws Exception {
        // given
        Article article = Article.builder()
                .title("test title")
                .author(user)
                .body("test body")
                .description("test desc")
                .build();
        Article savedArticle = articleRepository.save(article);
        String slug = savedArticle.getSlug();

        ArticleUpdateRequest request = new ArticleUpdateRequest();
        ReflectionTestUtils.setField(request, "title", "change title");
        ReflectionTestUtils.setField(request, "description", "change desc");
        ReflectionTestUtils.setField(request, "body", "change body");

        // then
        mockMvc.perform(put("/api/articles/{slug}", slug)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.title").value("change title"))
        .andExpect(jsonPath("$.description").value("change desc"))
        .andExpect(jsonPath("$.body").value("change body"));
    }

    @WithMockUser
    @DisplayName("게시글을 삭제한다.")
    @Test
    public void deleteArticle() throws Exception {
        // given
        Article article = Article.builder()
                .title("test title")
                .author(user)
                .body("test body")
                .description("test desc")
                .build();
        Article savedArticle = articleRepository.save(article);
        String slug = savedArticle.getSlug();

        // then
        mockMvc.perform(delete("/api/articles/{slug}", slug)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotHaveJsonPath());
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