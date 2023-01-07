package ifi.realworld.user.domain;

import ifi.realworld.common.entity.BaseUpdateInfoEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdateInfoEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String password;

    private String email;

    private String bio;

}