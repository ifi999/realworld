package ifi.realworld.user.api.dto;

import ifi.realworld.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileDto {

    private String username;
    private String bio;
    private String image;
    private boolean following;

    @Builder
    public ProfileDto(String username, String bio, String image, boolean following) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public static ProfileDto of(User user, Boolean followed) {
        return ProfileDto.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .following(followed)
                .build();
    }
}
