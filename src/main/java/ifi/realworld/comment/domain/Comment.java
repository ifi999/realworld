package ifi.realworld.comment.domain;

import ifi.realworld.article.domain.Article;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(length = 3000)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

}
