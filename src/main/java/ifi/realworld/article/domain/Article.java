package ifi.realworld.article.domain;

import ifi.realworld.comment.domain.Comment;
import ifi.realworld.common.entity.BaseUpdateInfoEntity;
import ifi.realworld.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "articles")
@ToString(of = {"id", "slug", "title", "description", "body", "author"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseUpdateInfoEntity {

    private static final long serialVersionUID = -560518392611816483L;

    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private Long id;

    @Column(length = 255)
    private String slug;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(length = 3000, nullable = false)
    private String body;

    @OneToOne(fetch = FetchType.LAZY)
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleTag> tagList = new ArrayList<>();

    @Builder
    public Article(String title, String description, String body, User author) {
        this.title = title;
        this.slug = setSlug(title);
        this.description = description;
        this.body = body;
        this.author = author;
    }

    private String setSlug(String title) {
        UUID uuid = UUID.randomUUID();
        String[] titleSplit = title.split(" ");
        if (titleSplit.length > 1) return uuid + "_" + titleSplit[0] + "-" + titleSplit[1];
        else return titleSplit[0];
    }

    public void editArticle(String title, String description, String body) {
        this.title = title;
        this.slug = setSlug(title);
        this.description = description;
        this.body = body;
    }

    public void editTag(List<ArticleTag> articleTags) {
        this.tagList = articleTags;
    }
}
