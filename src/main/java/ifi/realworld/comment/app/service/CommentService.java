package ifi.realworld.comment.app.service;

import ifi.realworld.comment.api.dto.CommentCreateRequest;
import ifi.realworld.comment.api.dto.CommentResponseDto;

public interface CommentService {

    CommentResponseDto createComments(String slug, CommentCreateRequest dto);

}
