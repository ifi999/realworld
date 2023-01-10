package ifi.realworld.follow.domain;

import ifi.realworld.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    private FollowType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User follower_id;

    // TODO - followee는 article, user 둘 다 대상인데 어떻게?

}
