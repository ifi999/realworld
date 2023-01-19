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
import ifi.realworld.common.exception.ArticleNotFoundException;
import ifi.realworld.common.exception.UserNotFoundException;
import ifi.realworld.common.security.CustomUserDetailsService;
import ifi.realworld.favorite.domain.repository.FavoriteJpaRepository;
import ifi.realworld.tag.domain.Tag;
import ifi.realworld.tag.domain.repository.TagRepository;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    public SingleArticleDto createArticles(ArticleCreateRequest dto) {
        User author = getAuthor();
        Article article = Article.builder()
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .body(dto.getBody())
                        .author(author)
                        .build();
        Article savedArticle = articleRepository.save(article);
        // TODO - 1. 매번 중복 태그 찾는 쿼리 돌리기가 좀 그런데 더 좋게 어떻게 해야할 지 모르겠음. 알아보기
        //      - 2. tags 이상함. Entity에서 처리해야할 것 같은데 이것도 어떻게 해야할 지 모르겠음.
        List<String> tagList = dto.getTagList();
        List<Tag> tags = new ArrayList<>();
        setArticleTag(article, tagList, tags);

        return SingleArticleDto.builder()
                .article(savedArticle)
                .tagList(tags)
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
        Article article = getArticleBySlug(slug);
        List<ArticleTag> articleTags = articleTagJpaRepository.findByArticleId(article.getId());
        List<Tag> tags = articleTags.stream()
                .map(o -> o.getTag())
                .collect(Collectors.toList());

        Boolean favorited = getFavorited(article);
        long favoriteCount = favoriteJpaRepository.articleFavoriteCount(article.getId());

        return SingleArticleDto.builder()
                .article(article)
                .tagList(tags)
                .author(article.getAuthor())
                .favoritesCount(favoriteCount)
                .favorited(favorited)
                .build();
    }

    @Override
    public SingleArticleDto updateArticle(String slug, ArticleUpdateRequest dto) {
        Article article = getArticleBySlug(slug);
        article.editArticle(dto.getTitle(), dto.getDescription(), dto.getBody());
        articleTagRepository.deleteAllInBatch(article.getTagList());
        // TODO - tagList를 변경감지로 하고 싶었는데 못하였음. 구조의 문제인지 내가 방식을 못 찾아낸건지 모르겠음

        List<String> tagList = dto.getTagList();
        List<Tag> tags = new ArrayList<>();
        List<ArticleTag> articleTags = setArticleTag(article, tagList, tags);
        article.editTag(articleTags);

        Boolean favorited = getFavorited(article);
        long favoriteCount = favoriteJpaRepository.articleFavoriteCount(article.getId());

        return SingleArticleDto.builder()
                .article(article)
                .tagList(tags)
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

    private Boolean getFavorited(Article article) {
        UserDetails currentUser = CustomUserDetailsService.getCurrentUserDetails();
        User user = userRepository.findByEmail(currentUser.getUsername()).orElseThrow(UserNotFoundException::new);
        Boolean favorited = favoriteJpaRepository.isFavorited(article.getId(), user.getId());
        return favorited;
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

    private User getAuthor() {
        UserDetails currentUserDetails = CustomUserDetailsService.getCurrentUserDetails();
        String currentUserEmail = currentUserDetails.getUsername();
        return userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new UserNotFoundException(currentUserEmail + " not found."));
    }
}
