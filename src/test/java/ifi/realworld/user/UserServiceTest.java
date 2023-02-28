package ifi.realworld.user;

import ifi.realworld.common.security.CustomUserDetailsService;
import ifi.realworld.common.security.jwt.JwtProvider;
import ifi.realworld.user.api.UserPasswordEncoder;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserCreateResponse;
import ifi.realworld.user.app.service.UserService;
import ifi.realworld.user.app.service.UserServiceImpl;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserPasswordEncoder userPasswordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private CustomUserDetailsService userDetailsService;

    private String header;

    private long validitySeconds;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userPasswordEncoder, jwtProvider, userDetailsService, header, validitySeconds);
    }

    @Test
    public void createUser_Success_test() {
        //given
        UserCreateRequest request = new UserCreateRequest();
        ReflectionTestUtils.setField(request, "username", "testUser");
        ReflectionTestUtils.setField(request, "email", "test@email.com");
        ReflectionTestUtils.setField(request, "password", "1234");

        //when
        UserCreateResponse response = userService.createUser(request);

        //then
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
    }

}
