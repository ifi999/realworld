package ifi.realworld.article.domain.repository;

import ifi.realworld.article.domain.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
}
