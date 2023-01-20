package ifi.realworld.comment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ifi.realworld.article.domain.Article;
import ifi.realworld.common.entity.BaseUpdateInfoEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseUpdateInfoEntity {

    private static final long serialVersionUID = -7223512392625279021L;

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(length = 3000, nullable = false)
    private String body;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    public Comment(String body, Article article) {
        this.body = body;
        this.article = article;
    }
}
