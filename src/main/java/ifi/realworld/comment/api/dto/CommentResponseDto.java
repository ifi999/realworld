package ifi.realworld.comment.api.dto;

import ifi.realworld.comment.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long id;
    private String body;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    // TODO - createBy 필요
//    private User author;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.body = comment.getBody();
        this.createAt = comment.getCreatedAt();
        this.updateAt = comment.getLastModifiedAt();
    }
}
