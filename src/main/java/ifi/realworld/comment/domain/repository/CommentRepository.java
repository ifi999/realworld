package ifi.realworld.comment.domain.repository;

import ifi.realworld.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
