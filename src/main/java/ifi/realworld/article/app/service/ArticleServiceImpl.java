package ifi.realworld.article.app.service;

import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.ArticleSearchDto;
import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.ArticleTag;
import ifi.realworld.article.domain.repository.ArticleJpaRepository;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.article.domain.repository.ArticleTagRepository;
import ifi.realworld.common.exception.UserNotFoundException;
import ifi.realworld.common.security.CustomUserDetailsService;
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

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleJpaRepository articleJpaRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

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
        if (tagList != null && !tagList.isEmpty()) {
            for(String t : tagList) {
                Tag tagEntity = createTag(new Tag(t));
                createArticleTag(article, tagEntity);
                tags.add(tagEntity);
            }
        }

        return SingleArticleDto.builder()
                .article(savedArticle)
                .tagList(tags)
                .author(savedArticle.getAuthor())
                .build();
    }

    @Override
    public Page<SingleArticleDto> getArticles(ArticleSearchDto search, Pageable pageable) {
        return articleJpaRepository.getArticles(search, pageable);
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
