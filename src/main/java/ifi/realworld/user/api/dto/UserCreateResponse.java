package ifi.realworld.user.api.dto;

import ifi.realworld.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateResponse {

    private String username;

    private String email;

    private String password;

    @Builder
    public UserCreateResponse(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static UserCreateResponse of(User user) {
        return UserCreateResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
