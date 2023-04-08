package ifi.realworld.article.app.service;

import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.ArticleUpdateRequest;
import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.ArticleTag;
import ifi.realworld.article.domain.repository.ArticleJpaRepository;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.article.domain.repository.ArticleTagJpaRepository;
import ifi.realworld.article.domain.repository.ArticleTagRepository;
import ifi.realworld.comment.domain.Comment;
import ifi.realworld.comment.domain.repository.CommentRepository;
import ifi.realworld.common.exception.api.ArticleNotFoundException;
import ifi.realworld.common.exception.api.UserNotFoundException;
import ifi.realworld.favorite.domain.repository.FavoriteJpaRepository;
import ifi.realworld.tag.domain.Tag;
import ifi.realworld.tag.domain.repository.TagRepository;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleJpaRepository articleJpaRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;
    private final ArticleTagJpaRepository articleTagJpaRepository;
    private final FavoriteJpaRepository favoriteJpaRepository;
    private final CommentRepository commentRepository;

    @Override
    public SingleArticleDto createArticles(ArticleCreateRequest dto) {
        User author = getAuthor(getCurrentUserEmail());
        Article article = Article.builder()
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .body(dto.getBody())
                        .author(author)
                        .build();
        Article savedArticle = articleRepository.save(article);

        List<String> tagList = dto.getTagList();
        List<Tag> tags = new ArrayList<>();
        setArticleTag(article, tagList, tags);

        return SingleArticleDto.builder()
                .article(savedArticle)
                .tagList(tags)
                .commentList(Collections.emptyList())
                .author(savedArticle.getAuthor())
                .favorited(false)
                .favoritesCount(0)
                .build();
    }

    @Override
    public Page<SingleArticleDto> getArticles(ArticleSearchDto search, Pageable pageable) {
        return articleJpaRepository.getArticles(search, pageable);
    }

    @Override
    public SingleArticleDto getArticle(String slug) {
        String currentUserEmail = getCurrentUserEmail();

        Article article = getArticleBySlug(slug);
        List<ArticleTag> articleTags = articleTagJpaRepository.findByArticleId(article.getId());
        List<Tag> tags = articleTags.stream()
                .map(o -> o.getTag())
                .collect(Collectors.toList());
        List<Comment> commentList = getCommentList(article);

        Boolean favorited = getFavorited(article, currentUserEmail);
        long favoriteCount = favoriteJpaRepository.articleFavoriteCount(article.getId());

        return SingleArticleDto.builder()
                .article(article)
                .tagList(tags)
                .commentList(commentList)
                .author(article.getAuthor())
                .favoritesCount(favoriteCount)
                .favorited(favorited)
                .build();
    }

    @Override
    public SingleArticleDto changeArticleInfo(String slug, ArticleUpdateRequest dto) {
        Article article = getArticleBySlug(slug);
        article.editArticle(dto.getTitle(), dto.getDescription(), dto.getBody());
        articleTagRepository.deleteAllInBatch(article.getTagList());

        List<String> tagList = dto.getTagList();
        List<Tag> tags = new ArrayList<>();
        List<ArticleTag> articleTags = setArticleTag(article, tagList, tags);
        article.editTag(articleTags);

        List<Comment> commentList = getCommentList(article);

        Boolean favorited = getFavorited(article, getCurrentUserEmail());
        long favoriteCount = favoriteJpaRepository.articleFavoriteCount(article.getId());

        return SingleArticleDto.builder()
                .article(article)
                .tagList(tags)
                .commentList(commentList)
                .author(article.getAuthor())
                .favorited(favorited)
                .favoritesCount(favoriteCount)
                .build();
    }

    @Override
    public void deleteArticle(String slug) {
        Article article = getArticleBySlug(slug);
        articleRepository.delete(article);
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private List<Comment> getCommentList(Article article) {
        return commentRepository.findByArticleId(article.getId());
    }

    private Boolean getFavorited(Article article, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return favoriteJpaRepository.isFavorited(article.getId(), user.getId());
    }

    private Article getArticleBySlug(String slug) {
        return articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
    }

    private List<ArticleTag> setArticleTag(Article article, List<String> tagList, List<Tag> tags) {
        List<ArticleTag> result = new ArrayList<>();
        if (tagList != null && !tagList.isEmpty()) {
            for (String t : tagList) {
                Tag tagEntity = createTag(new Tag(t));
                ArticleTag articleTag = createArticleTag(article, tagEntity);
                tags.add(tagEntity);
                result.add(articleTag);
            }
        }
        return result;
    }

    private ArticleTag createArticleTag(Article article, Tag tagEntity) {
        return articleTagRepository.save(new ArticleTag(article, tagEntity));
    }

    private Tag createTag(Tag tagEntity) {
        Optional<Tag> findTag = tagRepository.findByName(tagEntity.getName());
        if (findTag.isEmpty()) return tagRepository.save(tagEntity);
        else return findTag.orElseThrow();
    }

    private User getAuthor(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email + " not found."));
    }
}
