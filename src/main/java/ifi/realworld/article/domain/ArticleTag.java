package ifi.realworld.article.domain;

import ifi.realworld.common.entity.BaseCreateInfoEntity;
import ifi.realworld.tag.domain.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleTag extends BaseCreateInfoEntity {

    @Id
    @GeneratedValue
    @Column(name = "article_tag_id")
    private Long id;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Tag tag;


}
