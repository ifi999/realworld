package ifi.realworld.docs.favorite;

import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.docs.RestDocsSupport;
import ifi.realworld.favorite.api.FavoriteController;
import ifi.realworld.favorite.app.service.FavoriteService;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavoriteControllerDocsTest extends RestDocsSupport {

    private final ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);

    private final FavoriteService favoriteService = Mockito.mock(FavoriteService.class);

    @Override
    protected Object initController() {
        return new FavoriteController(favoriteService);
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

    @DisplayName("게시글 좋아요 추가 API")
    @Test
    public void favoriteArticle() throws Exception {
        // given
        String slug = article.getSlug();
        SingleArticleDto articleDto = getSingleArticleDto();

        given(favoriteService.favoriteArticle(any(String.class)))
                .willReturn(articleDto);

        // then
        mockMvc.perform(
                        post("/api/articles/{slug}/favorite", slug)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "favorite-article"
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

    @DisplayName("게시글 좋아요 취소 API")
    @Test
    public void unFavoriteArticle() throws Exception {
        // given
        String slug = article.getSlug();
        SingleArticleDto articleDto = getSingleArticleDto();

        given(favoriteService.unfavoriteArticle(any(String.class)))
                .willReturn(articleDto);

        // then
        mockMvc.perform(delete("/api/articles/{slug}/favorite", slug)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "unfavorite-article"
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

}