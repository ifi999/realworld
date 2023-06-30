package ifi.realworld.docs.comment;

import ifi.realworld.article.domain.Article;
import ifi.realworld.comment.api.CommentController;
import ifi.realworld.comment.api.dto.CommentCreateRequest;
import ifi.realworld.comment.api.dto.CommentResponseDto;
import ifi.realworld.comment.app.service.CommentService;
import ifi.realworld.comment.domain.Comment;
import ifi.realworld.comment.domain.repository.CommentRepository;
import ifi.realworld.docs.RestDocsSupport;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerDocsTest extends RestDocsSupport {

    private final CommentRepository commentRepository = Mockito.mock(CommentRepository.class);

    private final CommentService commentService = Mockito.mock(CommentService.class);

    @Override
    protected Object initController() {
        return new CommentController(commentService);
    }

    private final User user = User.builder()
            .email("test email")
            .username("테스트")
            .password("1234")
            .passwordEncoder(userPasswordEncoder)
            .build();

    private final Article article = Article.builder()
            .title("test title")
            .author(user)
            .body("test body")
            .description("test desc")
            .build();

    @DisplayName("게시글 코멘트 작성 API")
    @Test
    public void createComments() throws Exception {
        // given
        CommentCreateRequest request = new CommentCreateRequest();
        ReflectionTestUtils.setField(request, "body", "test comment");

        String slug = article.getSlug();

        Comment comment = new Comment(request.getBody(), article);

        given(commentService.createComments(any(String.class), any(CommentCreateRequest.class)))
                .willReturn(new CommentResponseDto(comment));

        // then
        mockMvc.perform(post("/api/articles/{slug}/comments", slug)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "create-comments"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , requestFields(
                        fieldWithPath("body")
                                .description("코멘트 내용")
                )
                , pathParameters(
                        parameterWithName("slug")
                                .description("게시글 제목")
                )
                , responseFields(
                        fieldWithPath("id").type(NUMBER)
                                .optional()
                                .description("코멘트 번호")
                        , fieldWithPath("body").type(STRING)
                                .optional()
                                .description("코멘트 내용")
                        , fieldWithPath("createAt").type(STRING)
                                .optional()
                                .description("코멘트 작성시간")
                        , fieldWithPath("updateAt").type(STRING)
                                .optional()
                                .description("코멘트 수정시간")
                )
        ));
    }

    @DisplayName("게시글 코멘트 목록 조회 API")
    @Test
    public void getComments() throws Exception {
        // given
        String slug = article.getSlug();

        Comment comment1 = new Comment("test comment 1", article);
        Comment comment2 = new Comment("test comment 2", article);
        Comment comment3 = new Comment("test comment 3", article);
        List<Comment> commentList = List.of(comment1, comment2, comment3);
        commentRepository.saveAll(commentList);

        List<CommentResponseDto> result = new ArrayList<>();
        commentList.stream()
                .forEach(o -> result.add(new CommentResponseDto(o)));

        given(commentService.getComments(any(String.class)))
                .willReturn(result);

        // then
        mockMvc.perform(get("/api/articles/{slug}/comments", slug)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "get-comments"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , pathParameters(
                        parameterWithName("slug")
                                .description("게시글 제목")
                )
                , responseFields(
                        fieldWithPath("[]id").type(NUMBER)
                                .optional()
                                .description("코멘트 번호")
                        , fieldWithPath("[]body").type(STRING)
                                .optional()
                                .description("코멘트 내용")
                        , fieldWithPath("[]createAt").type(STRING)
                                .optional()
                                .description("코멘트 작성시간")
                        , fieldWithPath("[]updateAt").type(STRING)
                                .optional()
                                .description("코멘트 수정시간")
                )
        ));
    }

    @DisplayName("게시글 코멘트 삭제 API")
    @Test
    public void deleteComments() throws Exception {
        // given
        String slug = article.getSlug();
        Long id = 0L;

        // then
        mockMvc.perform(delete("/api/articles/{slug}/comments/{id}", slug, 0L)
        )
        .andExpect(status().isOk())
        .andDo(document(
                "delete-comments"
                , preprocessRequest(prettyPrint())
                , pathParameters(
                        parameterWithName("slug")
                                .description("게시글 제목")
                        , parameterWithName("id")
                                .description("게시글 번호")
                )
        ));
    }

}