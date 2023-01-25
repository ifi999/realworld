package ifi.realworld.article.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import ifi.realworld.article.domain.Article;
import ifi.realworld.comment.domain.Comment;
import ifi.realworld.tag.domain.Tag;
import ifi.realworld.user.api.dto.UserInfoDto;
import ifi.realworld.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class SingleArticleDto {

    private Long id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private List<Tag> tagList = new ArrayList<>();
    private List<Comment> commentList = new ArrayList<>();
    private boolean favorited;
    private Long favoritesCount;
    private UserInfoDto author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public SingleArticleDto(Article article, List<Tag> tagList, List<Comment> commentList, User author, Boolean favorited, long favoritesCount) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.slug = article.setSlug(article.getTitle());
        this.description = article.getDescription();
        this.body = article.getBody();
        this.tagList = tagList;
        this.commentList = commentList;
        this.favorited = favorited;
        this.favoritesCount = favoritesCount;
        this.author = UserInfoDto.of(author);
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getLastModifiedAt();
    }

    @QueryProjection
    public SingleArticleDto(Long id, String slug, String title, String description, String body,  User author, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
//        this.tagList = tag;
        this.favorited = false; // 임시
        this.favoritesCount = 0l; // 임시
        this.author = UserInfoDto.of(author);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


}
