package ifi.realworld;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    protected JwtProvider jwtProvider;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected MockHttpServletResponse mockHttpServletResponse;

}
