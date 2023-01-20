package ifi.realworld.comment.app.service;

import ifi.realworld.comment.api.dto.CommentCreateRequest;
import ifi.realworld.comment.api.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto createComments(String slug, CommentCreateRequest dto);

    List<CommentResponseDto> getComments(String slug);

    void deleteComments(String slug, Long id);
}
