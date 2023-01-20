package ifi.realworld.comment.app.service;

import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.comment.api.dto.CommentCreateRequest;
import ifi.realworld.comment.api.dto.CommentResponseDto;
import ifi.realworld.comment.domain.Comment;
import ifi.realworld.comment.domain.repository.CommentRepository;
import ifi.realworld.common.exception.ArticleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Override
    public CommentResponseDto createComments(String slug, CommentCreateRequest dto) {
        Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
        Comment savedComment = commentRepository.save(new Comment(dto.getBody(), article));

        return new CommentResponseDto(savedComment.getBody());
    }
}
