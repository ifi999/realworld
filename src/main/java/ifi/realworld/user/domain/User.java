package ifi.realworld.user.domain;

import ifi.realworld.utils.entity.BaseUpdateInfoEntity;
import ifi.realworld.utils.security.UserPasswordEncoder;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@Entity
@ToString(of = {"id", "username", "email", "bio", "image"})
@Table(name = "users")
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdateInfoEntity {

    private static final long serialVersionUID = 3144631761026005885L;

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(length = 255, unique = true, nullable = false)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(length = 255)
    private String bio;

    @Column(length = 255)
    private String image;

    @Builder
    public User(String username, String password, UserPasswordEncoder passwordEncoder, String email) {
        Assert.notNull(username, "username must not be null.");
        Assert.notNull(email, "email must not be null.");
        if (passwordEncoder == null) {
            throw new IllegalArgumentException("passwordEncoder is null");
        }
        Assert.notNull(password, "password must not be null.");

        this.username = username;
        this.password = this.encodePassword(password, passwordEncoder);
        this.email = email;
    }

    private String encodePassword(String password, UserPasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }

    public boolean isMatched(String password, String encodedPassword, UserPasswordEncoder passwordEncoder) {
        boolean matched = passwordEncoder.matches(password, encodedPassword);
        if (!matched) {
            return false;
        }
        return true;
    }

    public void changeInfo(String username, String email, String password, UserPasswordEncoder passwordEncoder, String bio, String image) {
        this.username = username != null ? username : this.username;
        this.email = email != null ? email : this.email;
        this.bio = bio;
        this.image = image;

        if (passwordEncoder == null) {
            throw new IllegalArgumentException("passwordEncoder is null");
        }

        if(StringUtils.hasText(password) && !isMatched(password, this.getPassword(), passwordEncoder)) {
            this.password = this.encodePassword(password, passwordEncoder);
        }
    }
}