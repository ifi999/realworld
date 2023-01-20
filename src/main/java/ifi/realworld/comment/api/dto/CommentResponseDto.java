package ifi.realworld.comment.api.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {

    String body;

    public CommentResponseDto(String body) {
        this.body = body;
    }
}
