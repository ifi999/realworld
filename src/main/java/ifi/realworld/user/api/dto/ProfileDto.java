package ifi.realworld.user.api.dto;

import ifi.realworld.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

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

    public static ProfileDto of(Pair<User, Boolean> profile) {
        return ProfileDto.builder()
                .username(profile.getFirst().getUsername())
                .bio(profile.getFirst().getBio())
                .image(profile.getFirst().getImage())
                .following(profile.getSecond())
                .build();
    }
}
