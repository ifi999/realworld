package ifi.realworld.comment.api;

import ifi.realworld.comment.api.dto.CommentCreateRequest;
import ifi.realworld.comment.api.dto.CommentResponseDto;
import ifi.realworld.comment.app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Transactional
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/articles/{slug}/comments")
    public CommentResponseDto createComments(@PathVariable String slug, @RequestBody @Valid CommentCreateRequest dto) {
        return commentService.createComments(slug, dto);
    }

}
