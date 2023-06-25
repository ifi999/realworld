package ifi.realworld.api.comment.app.service;

import ifi.realworld.api.TestSupport;
import ifi.realworld.article.domain.Article;
import ifi.realworld.comment.api.dto.CommentCreateRequest;
import ifi.realworld.comment.api.dto.CommentResponseDto;
import ifi.realworld.comment.domain.Comment;
import ifi.realworld.user.domain.User;
import ifi.realworld.utils.exception.api.NotFoundCommentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentServiceTest extends TestSupport {

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
    
    @DisplayName("게시글에 코멘트를 등록한다.")
    @Test
    public void createComments() {
        // given
        String slug = article.getSlug();
        CommentCreateRequest request = new CommentCreateRequest();
        ReflectionTestUtils.setField(request, "body", "test comment");

        // when
        CommentResponseDto comment = commentService.createComments(slug, request);

        // then
        assertThat(comment.getBody()).isEqualTo(request.getBody());
    }

    @DisplayName("게시글의 코멘트 목록을 조회한다.")
    @Test
    public void getComments() {
        // given
        String slug = article.getSlug();

        Comment comment1 = new Comment("test comment1", article);
        Comment comment2 = new Comment("test comment2", article);
        Comment comment3 = new Comment("test comment3", article);
        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        // when
        List<CommentResponseDto> comments = commentService.getComments(slug);

        // then
        assertThat(comments).hasSize(3)
                .extracting("body")
                .contains("test comment1", "test comment2", "test comment3");
    }

    @DisplayName("게시글의 코멘트를 삭제한다.")
    @Test
    public void deleteComments() {
        // given
        String slug = article.getSlug();

        Comment comment = new Comment("test comment", article);
        Comment savedComment = commentRepository.save(comment);

        // when
        commentService.deleteComments(slug, savedComment.getId());

        // then
        List<CommentResponseDto> comments = commentService.getComments(slug);
        assertThat(comments).hasSize(0);
    }

    @DisplayName("게시글에 해당 코멘트가 존재하지 않다면 삭제에 실패한다.")
    @Test
    public void deleteCommentsWithNotFoundComment() {
        // given
        String slug = article.getSlug();

        // then
        assertThatThrownBy(() -> commentService.deleteComments(slug, 0l))
                .isInstanceOf(NotFoundCommentException.class)
                .hasMessage("This " + slug + " doesn't have this comment id " + 0l);
    }
}