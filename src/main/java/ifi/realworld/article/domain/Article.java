package ifi.realworld.article.domain;

import ifi.realworld.comment.domain.Comment;
import ifi.realworld.common.entity.BaseUpdateInfoEntity;
import ifi.realworld.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseUpdateInfoEntity {

    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private Long id;

    private String slug;

    private String title;

    private String description;

    private String body;

    @OneToOne(fetch = FetchType.LAZY)
    private User author;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

}
