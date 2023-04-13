package ifi.realworld.comment.app.service;

import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.comment.api.dto.CommentCreateRequest;
import ifi.realworld.comment.api.dto.CommentResponseDto;
import ifi.realworld.comment.domain.Comment;
import ifi.realworld.comment.domain.repository.CommentRepository;
import ifi.realworld.common.exception.api.ArticleNotFoundException;
import ifi.realworld.common.exception.api.NotFoundCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentResponseDto createComments(String slug, CommentCreateRequest dto) {
        Article article = getArticle(slug);
        Comment savedComment = commentRepository.save(new Comment(dto.getBody(), article));

        return new CommentResponseDto(savedComment);
    }

    public List<CommentResponseDto> getComments(String slug) {
        Article article = getArticle(slug);
        List<Comment> commentList = commentRepository.findByArticleId(article.getId());
        List<CommentResponseDto> result = new ArrayList<>();
        commentList.stream()
                .forEach(o -> result.add(new CommentResponseDto(o)));
        return result;
    }

    public void deleteComments(String slug, Long id) {
        Article article = getArticle(slug);
        Optional<Comment> findComment = commentRepository.findByIdAndArticleId(id, article);
        if (findComment.isEmpty()) throw new NotFoundCommentException("This " + slug + " doesn't have this comment id " + id);

        commentRepository.deleteByIdAndArticleId(id, article);
    }

    private Article getArticle(String slug) {
        return articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
    }
}
