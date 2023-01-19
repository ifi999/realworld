package ifi.realworld.favorite.api;

import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.favorite.app.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/articles/{slug}/favorite")
    public SingleArticleDto favoriteArticle(@AuthenticationPrincipal User currentUser, @PathVariable String slug) {
        // TODO - SingleArticleDto는 Article domain의 dto인데 따로 favoriteReponse 같이 dto를 만들어줘야하나?
        //        똑같은 내용일 것이라 재사용하고 싶은데

        return favoriteService.favoriteArticle(currentUser.getUsername(), slug);
    }

    @DeleteMapping("/articles/{slug}/favorite")
    public SingleArticleDto unfavoriteArticle(@AuthenticationPrincipal User currentUser, @PathVariable String slug) {
        return favoriteService.unfavoriteArticle(currentUser.getUsername(), slug);
    }

}
