package ifi.realworld.comment.domain.repository;

import ifi.realworld.article.domain.Article;
import ifi.realworld.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleId(Long id);

    @Modifying
    @Transactional
    @Query("delete from Comment c where c.id = :commentId and c.article = :article")
    void deleteByIdAndArticleId(@Param("commentId") Long commentId, @Param("article") Article article);

    @Query("select c from Comment c where c.id = :commentId and c.article = :article")
    Optional<Comment> findByIdAndArticleId(@Param("commentId") Long commentId, @Param("article") Article article);
}
