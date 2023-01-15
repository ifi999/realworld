package ifi.realworld.article.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.ArticleTag;
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
    private List<ArticleTag> tagList;
    private boolean favorited;
    private int favoritesCount;
    private UserInfoDto author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public SingleArticleDto(Article article, List<ArticleTag> tagList, User author) {
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

    @QueryProjection
    public SingleArticleDto(String slug, String title, String description, String body, List<ArticleTag> tagList, User author, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
        this.favorited = false; // 임시
        this.favoritesCount = 0; // 임시
        this.author = UserInfoDto.of(author);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


}
