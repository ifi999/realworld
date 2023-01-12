package ifi.realworld.article.app.service;

import ifi.realworld.article.api.dto.ArticleCreateRequest;
import ifi.realworld.article.api.dto.SingleArticleDto;
import ifi.realworld.article.domain.Article;
import ifi.realworld.article.domain.ArticleTag;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.article.domain.repository.ArticleTagRepository;
import ifi.realworld.common.exception.UserNotFoundException;
import ifi.realworld.common.security.CustomUserDetailsService;
import ifi.realworld.tag.domain.Tag;
import ifi.realworld.tag.domain.repository.TagRepository;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

    @Override
    public SingleArticleDto createArticles(ArticleCreateRequest dto) {
        User author = getAuthor();
        Article article = new Article(dto.getTitle(), dto.getDescription(), dto.getBody(), author);
        Article savedArticle = articleRepository.save(article);
        // TODO - 매번 중복 태그 찾는 쿼리 돌리기가 좀 그런데 더 좋게 어떻게 해야할 지 모르겠음. 알아보기
        List<String> tagList = dto.getTagList();
        for(String tag : tagList) {
            Tag tagEntity = new Tag(tag);
            createTag(tagEntity);
            createArticleTag(article, tagEntity);
        }

        return SingleArticleDto.builder()
                .article(savedArticle)
                .tagList(tagList)
                .author(savedArticle.getAuthor())
                .build();
    }

    private void createArticleTag(Article article, Tag tagEntity) {
        articleTagRepository.save(new ArticleTag(article, tagEntity));
    }

    private void createTag(Tag tagEntity) {
        Optional<Tag> findTag = tagRepository.findByName(tagEntity.getName());
        if (findTag.isEmpty()) tagRepository.save(tagEntity);
    }

    private User getAuthor() {
        UserDetails currentUserDetails = CustomUserDetailsService.getCurrentUserDetails();
        String currentUserEmail = currentUserDetails.getUsername();
        return userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new UserNotFoundException(currentUserEmail + " not found."));
    }
}
