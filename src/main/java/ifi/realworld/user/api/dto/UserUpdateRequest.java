package ifi.realworld.user.api.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UserUpdateRequest {

    @Length(max = 255)
    private String email;

    @Length(max = 255)
    private String username;

    @Length(max = 255)
    private String password;

    @Length(max = 255)
    private String image;

    @Length(max = 255)
    private String bio;

    @Builder
    public UserUpdateRequest(String email, String username, String password, String image, String bio) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.image = image;
        this.bio = bio;
    }
}