package ifi.realworld.favorite.domain;

import ifi.realworld.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

    @Id
    @GeneratedValue
    @Column(name = "favorite_id")
    private Long id;

    private FavoriteType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User follower_id;

    // TODO - followee는 article, user 둘 다 대상인데 어떻게?

}
