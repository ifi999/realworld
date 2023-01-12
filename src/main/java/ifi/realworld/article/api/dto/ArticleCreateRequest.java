package ifi.realworld.article.api.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ArticleCreateRequest {

    private String title;

    private String description;

    private String body;

    private List<String> tagList;

}
