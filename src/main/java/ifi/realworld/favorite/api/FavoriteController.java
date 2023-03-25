package ifi.realworld.favorite.api;

import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.favorite.app.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/articles/{slug}/favorite")
    public ResponseEntity<SingleArticleDto> favoriteArticle(@PathVariable String slug) {
        SingleArticleDto response = favoriteService.favoriteArticle(slug);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/articles/{slug}/favorite")
    public ResponseEntity<SingleArticleDto> unFavoriteArticle(@PathVariable String slug) {
        SingleArticleDto response = favoriteService.unfavoriteArticle(slug);
        return ResponseEntity.ok(response);
    }

}
