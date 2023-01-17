package ifi.realworld.article.api.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
public class ArticleCreateRequest {

    @NotEmpty
    @Length(max = 255)
    private String title;

    @NotEmpty
    @Length(max = 255)
    private String description;

    @NotEmpty
    @Length(max = 3000)
    private String body;

    private List<String> tagList;

}
