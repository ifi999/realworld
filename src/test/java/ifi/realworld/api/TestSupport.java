package ifi.realworld.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifi.realworld.article.app.service.ArticleService;
import ifi.realworld.article.domain.repository.ArticleJpaRepository;
import ifi.realworld.article.domain.repository.ArticleRepository;
import ifi.realworld.article.domain.repository.ArticleTagJpaRepository;
import ifi.realworld.article.domain.repository.ArticleTagRepository;
import ifi.realworld.comment.app.service.CommentService;
import ifi.realworld.comment.domain.repository.CommentRepository;
import ifi.realworld.favorite.app.service.FavoriteService;
import ifi.realworld.favorite.domain.repository.FavoriteJpaRepository;
import ifi.realworld.favorite.domain.repository.FavoriteRepository;
import ifi.realworld.tag.domain.repository.TagRepository;
import ifi.realworld.user.app.service.ProfileService;
import ifi.realworld.user.app.service.UserService;
import ifi.realworld.user.domain.repository.FollowJpaRepository;
import ifi.realworld.user.domain.repository.FollowRepository;
import ifi.realworld.user.domain.repository.UserRepository;
import ifi.realworld.utils.security.UserPasswordEncoder;
import ifi.realworld.utils.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public abstract class TestSupport {

    @Autowired
    protected UserPasswordEncoder userPasswordEncoder;

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected FollowJpaRepository followJpaRepository;

    @Autowired
    protected FollowRepository followRepository;

    @Autowired
    protected ProfileService profileService;

    @Autowired
    protected TagRepository tagRepository;

    @Autowired
    protected FavoriteRepository favoriteRepository;

    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected ArticleService articleService;

    @Autowired
    protected FavoriteService favoriteService;

    @Autowired
    protected FavoriteJpaRepository favoriteJpaRepository;

    @Autowired
    protected CommentService commentService;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected ArticleJpaRepository articleJpaRepository;

    @Autowired
    protected ArticleTagRepository articleTagRepository;

    @Autowired
    protected ArticleTagJpaRepository articleTagJpaRepository;

    @Autowired
    protected JwtProvider jwtProvider;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected MockHttpServletResponse mockHttpServletResponse;

}
