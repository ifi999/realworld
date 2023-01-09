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

    private static final long serialVersionUID = -560518392611816483L;

    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private Long id;

    @Column(length = 255, nullable = false)
    private String slug;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(length = 3000, nullable = false)
    private String body;

    @OneToOne(fetch = FetchType.LAZY)
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

}
