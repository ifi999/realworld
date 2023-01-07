package ifi.realworld.user.api.dto;

import ifi.realworld.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class UserLoginDto {

    @NotEmpty
    @Length(max = 255)
    private String email;

    @NotEmpty
    @Length(max = 255)
    private String password;

    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserLoginDto of(User user) {
        return new UserLoginDto(user.getEmail(), user.getPassword());
    }
}
