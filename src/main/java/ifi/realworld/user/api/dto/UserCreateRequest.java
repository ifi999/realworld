package ifi.realworld.user.api.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
public class UserCreateRequest {

    @NotEmpty
    @Length(max = 255)
    private String username;

    @NotEmpty
    @Length(max = 255)
    private String email;

    @NotEmpty
    @Length(max = 255)
    private String password;

}
