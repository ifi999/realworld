package ifi.realworld.article.api;

import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.app.service.ArticleService;
import lombok.RequiredArgsConstructor;
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
    public SingleArticleDto CreateArticle(@RequestBody @Valid final ArticleCreateRequest dto) {
        return articleService.createArticles(dto);
    }

}
