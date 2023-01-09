package ifi.realworld.user.api.dto;

import ifi.realworld.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoDto {

    private String email;
    private String username;
    private String bio;
    private String image;

    @Builder
    public UserInfoDto(String email, String token, String username, String bio, String image) {
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.image = image;
    }

    public static UserInfoDto of(User user) {
        return UserInfoDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }

}
