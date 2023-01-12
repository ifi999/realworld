package ifi.realworld.article.api.dto;

import ifi.realworld.article.domain.Article;
import ifi.realworld.user.api.dto.UserInfoDto;
import ifi.realworld.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class SingleArticleDto {

    private String slug;
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
    private boolean favorited;
    private int favoritesCount;
    private UserInfoDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public SingleArticleDto(Article article, List<String> tagList, User author) {
        this.slug = article.getSlug();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.body = article.getBody();
        this.tagList = tagList;
        this.favorited = false;
        this.favoritesCount = 0;
        this.author = UserInfoDto.of(author);
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getLastModifiedAt();
    }
}
