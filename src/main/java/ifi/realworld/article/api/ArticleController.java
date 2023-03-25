package ifi.realworld.article.api;

import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.ArticleUpdateRequest;
import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.app.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Transactional
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/articles")
    public ResponseEntity<SingleArticleDto> createArticle(@RequestBody @Valid final ArticleCreateRequest dto) {
        SingleArticleDto response = articleService.createArticles(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/articles")
    public ResponseEntity<Page<SingleArticleDto>> getArticles(final ArticleSearchDto dto, Pageable pageable) {
        Page<SingleArticleDto> response = articleService.getArticles(dto, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/articles/{slug}")
    public ResponseEntity<SingleArticleDto> getArticle(@PathVariable String slug) {
        SingleArticleDto response = articleService.getArticle(slug);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/articles/{slug}")
    public ResponseEntity<SingleArticleDto> changeArticleInfo(@PathVariable String slug
            , @RequestBody @Valid final ArticleUpdateRequest dto) {
        SingleArticleDto response = articleService.changeArticleInfo(slug, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/articles/{slug}")
    public void deleteArticle(@PathVariable String slug) {
        articleService.deleteArticle(slug);
    }

}
