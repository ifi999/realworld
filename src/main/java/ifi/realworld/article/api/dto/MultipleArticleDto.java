package ifi.realworld.article.api.dto;

import ifi.realworld.article.domain.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MultipleArticleDto {

    private List<SingleArticleDto> articles;
    private int articlesCount;

    public MultipleArticleDto(List<SingleArticleDto> articles, int articlesCount) {
        this.articles = articles;
        this.articlesCount = articlesCount;
    }
}
