package ifi.realworld.tag.domain;

import ifi.realworld.common.entity.BaseCreateInfoEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseCreateInfoEntity {


    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(length = 255)
    private String name;

}
