package ifi.realworld.docs.article;

import ifi.realworld.article.api.ArticleController;
import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.ArticleUpdateRequest;
import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.app.service.ArticleService;
import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.docs.RestCodsSupport;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ArticleControllerDocsTest extends RestCodsSupport {

    private final ArticleService articleService = Mockito.mock(ArticleService.class);

    private final ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);

    @Override
    protected Object initController() {
        return new ArticleController(articleService);
    }

    private final User user = User.builder()
            .email("test email")
            .username("테스트")
            .password("1234")
            .passwordEncoder(userPasswordEncoder)
            .build();

    @DisplayName("게시글 작성 API")
    @Test
    public void createArticle() throws Exception {
        // given
        ArticleCreateRequest request = new ArticleCreateRequest();
        ReflectionTestUtils.setField(request, "title", "test title");
        ReflectionTestUtils.setField(request, "description", "test description");
        ReflectionTestUtils.setField(request, "body", "test body");

        SingleArticleDto articleDto = getSingleArticleDto();

        given(articleService.createArticles(any(ArticleCreateRequest.class)))
                .willReturn(articleDto);

        // then
        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
           "create-article"
           , preprocessRequest(prettyPrint())
           , preprocessResponse(prettyPrint())
           , requestFields(
                   fieldWithPath("title").type(STRING)
                           .description("게시글 제목")
                   , fieldWithPath("description").type(STRING)
                            .description("게시글 서술")
                   , fieldWithPath("body").type(STRING)
                            .description("게시글 내용")
                   , fieldWithPath("tagList").type(STRING)
                            .optional()
                            .description("추가할 태그 목록")
           )
           , responseFields(
                   fieldWithPath("id").type(NUMBER)
                           .optional()
                           .description("게시글 번호")
                   , fieldWithPath("slug").type(STRING)
                           .description("게시글 제목 요약")
                   , fieldWithPath("title").type(STRING)
                           .description("게시글 제목")
                   , fieldWithPath("description").type(STRING)
                           .description("게시글 서술")
                   , fieldWithPath("body").type(STRING)
                           .description("게시글 내용")
                   , fieldWithPath("tagList").type(ARRAY)
                           .optional()
                           .description("태그 목록")
                   , fieldWithPath("tagList[].id").type(NUMBER)
                           .description("태그 번호")
                   , fieldWithPath("tagList[].name").type(STRING)
                           .description("태그명")
                   , fieldWithPath("commentList").type(ARRAY)
                           .optional()
                           .description("댓글 목록")
                   , fieldWithPath("commentList[].id").type(NUMBER)
                           .description("댓글 번호")
                   , fieldWithPath("commentList[].body").type(STRING)
                           .description("댓글 내용")
                   , fieldWithPath("favorited").type(BOOLEAN)
                           .description("좋아요 여부")
                   , fieldWithPath("favoritesCount").type(NUMBER)
                           .description("좋아요 수")
                   , fieldWithPath("author").type(OBJECT)
                           .description("작성자")
                   , fieldWithPath("author.email").type(STRING)
                           .description("이메일")
                   , fieldWithPath("author.username").type(STRING)
                           .description("작성자 이름")
                   , fieldWithPath("author.bio").type(STRING)
                           .optional()
                           .description("작성자 소개")
                   , fieldWithPath("author.image").type(STRING)
                           .optional()
                           .description("작성자 프로필 사진")
                   , fieldWithPath("createdAt").type(STRING)
                           .optional()
                           .description("작성 시간")
                   , fieldWithPath("updatedAt").type(STRING)
                           .optional()
                           .description("수정 시간")
           )
        ));
    }

    @DisplayName("게시글 목록 조회 API")
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

        SingleArticleDto articleDto = SingleArticleDto.builder()
                .article(Article.builder()
                        .title("test title")
                        .description("test description")
                        .body("test body")
                        .author(user)
                        .build())
                .author(user)
                .commentList(Collections.emptyList())
                .tagList(Collections.emptyList())
                .favorited(false)
                .favoritesCount(5)
                .build();

        given(articleService.getArticles(any(ArticleSearchDto.class), any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(articleDto), pageable, 1));

        // then
        mockMvc.perform(get("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize()))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "get-articles"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , requestFields(
                        fieldWithPath("tag").type(STRING)
                                .optional()
                                .description("게시글 태그")
                        , fieldWithPath("author").type(STRING)
                                .description("게시글 작성자")
                )
                , requestParameters(
                        parameterWithName("page")
                                .optional()
                                .description("게시글 페이지 번호")
                        , parameterWithName("size")
                                .optional()
                                .description("게시글 페이지 크기")
                )
                , responseFields(
                        fieldWithPath("content").type(ARRAY)
                                .optional()
                                .description("내용")
                        , fieldWithPath("content[].id").type(NUMBER)
                                .optional()
                                .description("게시글 번호")
                        , fieldWithPath("content[].slug").type(STRING)
                                .description("게시글 제목 요약")
                        , fieldWithPath("content[].title").type(STRING)
                                .description("게시글 제목")
                        , fieldWithPath("content[].description").type(STRING)
                                .description("게시글 서술")
                        , fieldWithPath("content[].body").type(STRING)
                                .description("게시글 내용")
                        , fieldWithPath("content[].tagList").type(ARRAY)
                                .optional()
                                .description("태그 목록")
                        , fieldWithPath("content[].tagList[].id").type(NUMBER)
                                .description("태그 번호")
                        , fieldWithPath("content[].tagList[].name").type(STRING)
                                .description("태그명")
                        , fieldWithPath("content[].commentList").type(ARRAY)
                                .optional()
                                .description("댓글 목록")
                        , fieldWithPath("content[].commentList[].id").type(NUMBER)
                                .description("댓글 번호")
                        , fieldWithPath("content[].commentList[].body").type(STRING)
                                .description("댓글 내용")
                        , fieldWithPath("content[].favorited").type(BOOLEAN)
                                .description("좋아요 여부")
                        , fieldWithPath("content[].favoritesCount").type(NUMBER)
                                .description("좋아요 수")
                        , fieldWithPath("content[].author").type(OBJECT)
                                .description("작성자")
                        , fieldWithPath("content[].author.email").type(STRING)
                                .description("이메일")
                        , fieldWithPath("content[].author.username").type(STRING)
                                .description("작성자 이름")
                        , fieldWithPath("content[].author.bio").type(STRING)
                                .optional()
                                .description("작성자 소개")
                        , fieldWithPath("content[].author.image").type(STRING)
                                .optional()
                                .description("작성자 프로필 사진")
                        , fieldWithPath("content[].createdAt").type(STRING)
                                .optional()
                                .description("작성 시간")
                        , fieldWithPath("content[].updatedAt").type(STRING)
                                .optional()
                                .description("수정 시간")
                        , fieldWithPath("pageable").type(OBJECT)
                                .optional()
                                .description("페이징 방식")
                        , fieldWithPath("pageable.sort").type(OBJECT)
                                .optional()
                                .description("정렬 방식")
                        , fieldWithPath("pageable.sort.empty").type(BOOLEAN)
                                .optional()
                                .description("empty")
                        , fieldWithPath("pageable.sort.sorted").type(BOOLEAN)
                                .optional()
                                .description("정렬 ㅇ")
                        , fieldWithPath("pageable.sort.unsorted").type(BOOLEAN)
                                .optional()
                                .description("정렬 ㄴ")
                        , fieldWithPath("pageable.offset").type(NUMBER)
                                .optional()
                                .description("오프셋 값")
                        , fieldWithPath("pageable.pageSize").type(NUMBER)
                                .optional()
                                .description("페이지 크기 값")
                        , fieldWithPath("pageable.pageNumber").type(NUMBER)
                                .optional()
                                .description("페이지 번 값")
                        , fieldWithPath("pageable.paged").type(BOOLEAN)
                                .optional()
                                .description("페이징 여부1")
                        , fieldWithPath("pageable.unpaged").type(BOOLEAN)
                                .optional()
                                .description("페이지 여부2")
                        , fieldWithPath("last").type(BOOLEAN)
                                .optional()
                                .description("마지막 페이지 체크 여부")
                        , fieldWithPath("totalPages").type(NUMBER)
                                .optional()
                                .description("총 페이지 값")
                        , fieldWithPath("totalElements").type(NUMBER)
                                .optional()
                                .description("총 요소 값")
                        , fieldWithPath("size").type(NUMBER)
                                .optional()
                                .description("페이지 사이즈")
                        , fieldWithPath("number").type(NUMBER)
                                .optional()
                                .description("페이지 번호")
                        , fieldWithPath("sort").type(OBJECT)
                                .optional()
                                .description("정렬 방식")
                        , fieldWithPath("sort.empty").type(BOOLEAN)
                                .optional()
                                .description("empty")
                        , fieldWithPath("sort.sorted").type(BOOLEAN)
                                .optional()
                                .description("정렬 ㅇ")
                        , fieldWithPath("sort.unsorted").type(BOOLEAN)
                                .optional()
                                .description("정렬 ㄴ")
                        , fieldWithPath("first").type(BOOLEAN)
                                .optional()
                                .description("첫 페이지 체크 여부")
                        , fieldWithPath("numberOfElements").type(NUMBER)
                                .optional()
                                .description("총 요소 값")
                        , fieldWithPath("empty").type(BOOLEAN)
                                .optional()
                                .description("empty")
                )
        ));
    }

    @DisplayName("게시글 조회 API")
    @Test
    public void getArticle() throws Exception {
        // given
        Article article = Article.builder()
                .title("test title")
                .author(user)
                .body("test body")
                .description("test desc")
                .build();

        String slug = article.getSlug();

        SingleArticleDto articleDto = getSingleArticleDto();

        given(articleService.getArticle(any(String.class)))
                .willReturn(articleDto);

        // then
        mockMvc.perform(get("/api/articles/{slug}", slug)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "get-article"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , pathParameters(
                        parameterWithName("slug")
                                .description("게시글 제목")
                )
                , responseFields(
                        fieldWithPath("id").type(NUMBER)
                                .optional()
                                .description("게시글 번호")
                        , fieldWithPath("slug").type(STRING)
                                .description("게시글 제목 요약")
                        , fieldWithPath("title").type(STRING)
                                .description("게시글 제목")
                        , fieldWithPath("description").type(STRING)
                                .description("게시글 서술")
                        , fieldWithPath("body").type(STRING)
                                .description("게시글 내용")
                        , fieldWithPath("tagList").type(ARRAY)
                                .optional()
                                .description("태그 목록")
                        , fieldWithPath("tagList[].id").type(NUMBER)
                                .description("태그 번호")
                        , fieldWithPath("tagList[].name").type(STRING)
                                .description("태그명")
                        , fieldWithPath("commentList").type(ARRAY)
                                .optional()
                                .description("댓글 목록")
                        , fieldWithPath("commentList[].id").type(NUMBER)
                                .description("댓글 번호")
                        , fieldWithPath("commentList[].body").type(STRING)
                                .description("댓글 내용")
                        , fieldWithPath("favorited").type(BOOLEAN)
                                .description("좋아요 여부")
                        , fieldWithPath("favoritesCount").type(NUMBER)
                                .description("좋아요 수")
                        , fieldWithPath("author").type(OBJECT)
                                .description("작성자")
                        , fieldWithPath("author.email").type(STRING)
                                .description("이메일")
                        , fieldWithPath("author.username").type(STRING)
                                .description("작성자 이름")
                        , fieldWithPath("author.bio").type(STRING)
                                .optional()
                                .description("작성자 소개")
                        , fieldWithPath("author.image").type(STRING)
                                .optional()
                                .description("작성자 프로필 사진")
                        , fieldWithPath("createdAt").type(STRING)
                                .optional()
                                .description("작성 시간")
                        , fieldWithPath("updatedAt").type(STRING)
                                .optional()
                                .description("수정 시간")
                )
        ));
    }

    @DisplayName("게시글 수정 API")
    @Test
    public void changeArticleInfo() throws Exception {
        // given
        Article article = Article.builder()
                .title("test title")
                .author(user)
                .body("test body")
                .description("test desc")
                .build();

        String slug = article.getSlug();
        SingleArticleDto articleDto = getSingleArticleDto();

        ArticleUpdateRequest request = new ArticleUpdateRequest();
        ReflectionTestUtils.setField(request, "title", "change title");
        ReflectionTestUtils.setField(request, "description", "change desc");
        ReflectionTestUtils.setField(request, "body", "change body");

        given(articleService.changeArticleInfo(any(String.class), any(ArticleUpdateRequest.class)))
                .willReturn(articleDto);

        // then
        mockMvc.perform(put("/api/articles/{slug}", slug)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "change-article-info"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , pathParameters(
                        parameterWithName("slug")
                                .description("게시글 제목")
                )
                , requestFields(
                        fieldWithPath("title").type(STRING)
                                .description("게시글 제목")
                        , fieldWithPath("description").type(STRING)
                                .description("게시글 서술")
                        , fieldWithPath("body").type(STRING)
                                .description("게시글 내용")
                        , fieldWithPath("tagList").type(STRING)
                                .optional()
                                .description("추가할 태그 목록")
                )
                , responseFields(
                        fieldWithPath("id").type(NUMBER)
                                .optional()
                                .description("게시글 번호")
                        , fieldWithPath("slug").type(STRING)
                                .description("게시글 제목 요약")
                        , fieldWithPath("title").type(STRING)
                                .description("게시글 제목")
                        , fieldWithPath("description").type(STRING)
                                .description("게시글 서술")
                        , fieldWithPath("body").type(STRING)
                                .description("게시글 내용")
                        , fieldWithPath("tagList").type(ARRAY)
                                .optional()
                                .description("태그 목록")
                        , fieldWithPath("tagList[].id").type(NUMBER)
                                .description("태그 번호")
                        , fieldWithPath("tagList[].name").type(STRING)
                                .description("태그명")
                        , fieldWithPath("commentList").type(ARRAY)
                                .optional()
                                .description("댓글 목록")
                        , fieldWithPath("commentList[].id").type(NUMBER)
                                .description("댓글 번호")
                        , fieldWithPath("commentList[].body").type(STRING)
                                .description("댓글 내용")
                        , fieldWithPath("favorited").type(BOOLEAN)
                                .description("좋아요 여부")
                        , fieldWithPath("favoritesCount").type(NUMBER)
                                .description("좋아요 수")
                        , fieldWithPath("author").type(OBJECT)
                                .description("작성자")
                        , fieldWithPath("author.email").type(STRING)
                                .description("이메일")
                        , fieldWithPath("author.username").type(STRING)
                                .description("작성자 이름")
                        , fieldWithPath("author.bio").type(STRING)
                                .optional()
                                .description("작성자 소개")
                        , fieldWithPath("author.image").type(STRING)
                                .optional()
                                .description("작성자 프로필 사진")
                        , fieldWithPath("createdAt").type(STRING)
                                .optional()
                                .description("작성 시간")
                        , fieldWithPath("updatedAt").type(STRING)
                                .optional()
                                .description("수정 시간")
                )
        ));
    }

    @DisplayName("게시글 삭제 API")
    @Test
    public void deleteArticle() throws Exception {
        // given
        Article article = Article.builder()
                .title("test title")
                .author(user)
                .body("test body")
                .description("test desc")
                .build();

        String slug = article.getSlug();

        // then
        mockMvc.perform(delete("/api/articles/{slug}", slug)
        )
        .andExpect(status().isOk())
                .andDo(document(
                        "delete-article"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(
                                parameterWithName("slug")
                                        .description("게시글 제목")
                        )
                ));
    }

    private SingleArticleDto getSingleArticleDto() {
        SingleArticleDto articleDto = SingleArticleDto.builder()
                .article(Article.builder()
                        .title("test title")
                        .description("test description")
                        .body("test body")
                        .author(user)
                        .build())
                .author(user)
                .commentList(Collections.emptyList())
                .tagList(Collections.emptyList())
                .favorited(false)
                .favoritesCount(5)
                .build();
        return articleDto;
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