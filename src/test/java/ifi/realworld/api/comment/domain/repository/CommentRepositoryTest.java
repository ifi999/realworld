package ifi.realworld.api.comment.domain.repository;

import ifi.realworld.api.TestSupport;
import ifi.realworld.article.domain.Article;
import ifi.realworld.comment.domain.Comment;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CommentRepositoryTest extends TestSupport {

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

    @DisplayName("게시글 번호로 코멘트 목록을 조회한다.")
    @Test
    public void findByArticleId() {
        // given
        Long id = article.getId();

        Comment comment1 = new Comment("test comment1", article);
        Comment comment2 = new Comment("test comment2", article);
        Comment comment3 = new Comment("test comment3", article);
        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        // when
        List<Comment> comments = commentRepository.findByArticleId(id);

        // then
        assertThat(comments).hasSize(3)
                .extracting("body")
                .contains("test comment1", "test comment2", "test comment3");
    }

    @DisplayName("게시글 번호와 코멘트 번호로 해당 코멘트를 삭제한다.")
    @Test
    public void deleteByIdAndArticleId() {
        // given
        Comment comment = new Comment("test comment", article);
        Comment savedComment = commentRepository.save(comment);
        Long commentId = savedComment.getId();

        // when
        commentRepository.deleteByIdAndArticleId(commentId, article);

        // then
        Long id = article.getId();
        List<Comment> comments = commentRepository.findByArticleId(article.getId());
        assertThat(comments).hasSize(0);
    }

    @DisplayName("게시글 번호와 코멘트 번호로 해당 코멘트를 조회한다.")
    @Test
    public void findByIdAndArticleId() {
        // given
        Comment comment = new Comment("test comment", article);
        Comment savedComment = commentRepository.save(comment);
        Long commentId = savedComment.getId();

        // when
        Optional<Comment> articleComment = commentRepository.findByIdAndArticleId(commentId, article);

        // then
        assertThat(articleComment).isPresent();
        assertThat(articleComment.get().getBody()).isEqualTo("test comment");
    }

}