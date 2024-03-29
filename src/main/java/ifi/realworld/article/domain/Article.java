package ifi.realworld.article.domain;

import ifi.realworld.comment.domain.Comment;
import ifi.realworld.utils.entity.BaseUpdateInfoEntity;
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
        this.slug = setSlug();
        this.description = description;
        this.body = body;
        this.author = author;
    }

    public String setSlug() {
        UUID uuid = UUID.randomUUID();
        String[] titleSplit = this.title.split(" ");
        if (titleSplit.length > 1) return titleSplit[0] + "-" + titleSplit[1] + "_" + uuid;
        else return titleSplit[0] + "_" + uuid;
    }

    public void editArticle(String title, String description, String body) {
        this.title = title;
        this.slug = setSlug();
        this.description = description;
        this.body = body;
    }

    public void editTag(List<ArticleTag> articleTags) {
        this.tagList = articleTags;
    }
}
