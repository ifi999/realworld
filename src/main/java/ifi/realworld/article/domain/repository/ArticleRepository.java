package ifi.realworld.article.domain.repository;

import ifi.realworld.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
