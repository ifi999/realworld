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
        // TODO - SingleArticleDto는 Article domain의 dto인데 따로 favoriteReponse 같이 dto를 만들어줘야하나?
        //        똑같은 내용일 것이라 재사용하고 싶은데

        SingleArticleDto response = favoriteService.favoriteArticle(slug);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/articles/{slug}/favorite")
    public ResponseEntity<SingleArticleDto> unfavoriteArticle(@PathVariable String slug) {
        SingleArticleDto response = favoriteService.unfavoriteArticle(slug);
        return ResponseEntity.ok(response);
    }

}
