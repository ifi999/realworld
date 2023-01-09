package ifi.realworld.user.domain;

import ifi.realworld.common.entity.BaseUpdateInfoEntity;
import ifi.realworld.common.exception.PasswordNotMatchedException;
import ifi.realworld.user.api.UserPasswordEncoder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdateInfoEntity {

    private static final long serialVersionUID = 3144631761026005885L;

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(length = 255, nullable = false)
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
        this.password = encodePassword(password, passwordEncoder);
        this.email = email;
    }

    private static String encodePassword(String password, UserPasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }

    public void isMatched(String password, String encodedPassword, UserPasswordEncoder passwordEncoder) {
        boolean matched = passwordEncoder.matches(password, encodedPassword);
        if (!matched) {
            throw new PasswordNotMatchedException(this.getEmail());
        }
    }
}