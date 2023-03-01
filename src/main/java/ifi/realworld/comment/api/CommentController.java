package ifi.realworld.comment.api;

import ifi.realworld.comment.api.dto.CommentCreateRequest;
import ifi.realworld.comment.api.dto.CommentResponseDto;
import ifi.realworld.comment.app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Transactional
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/articles/{slug}/comments")
    public ResponseEntity<CommentResponseDto> createComments(@PathVariable String slug, @RequestBody @Valid CommentCreateRequest dto) {
        CommentResponseDto response = commentService.createComments(slug, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/articles/{slug}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable String slug) {
        List<CommentResponseDto> response = commentService.getComments(slug);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/articles/{slug}/comments/{id}")
    public void deleteComments(@PathVariable String slug, @PathVariable Long id) {
        commentService.deleteComments(slug, id);
    }

}
