package ifi.realworld.tag.domain;

import ifi.realworld.utils.entity.BaseCreateInfoEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@ToString(of = "name")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseCreateInfoEntity {

    private static final long serialVersionUID = -8744880418545929863L;

    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
