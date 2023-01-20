package ifi.realworld.comment.api.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
public class CommentCreateRequest {

    @NotEmpty
    @Length(max = 3000)
    private String body;

}
