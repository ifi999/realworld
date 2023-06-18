package ifi.realworld.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserLoginDto;
import ifi.realworld.user.api.dto.UserUpdateRequest;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import ifi.realworld.utils.security.UserPasswordEncoder;
import ifi.realworld.utils.security.jwt.JwtProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPasswordEncoder userPasswordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email("test email")
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .username("테스트")
                .build();
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("신규 유저를 등록한다.")
    @Test
    public void createUserIsOk() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        ReflectionTestUtils.setField(request, "username", "test2 user");
        ReflectionTestUtils.setField(request, "email", "test2@hello.com");
        ReflectionTestUtils.setField(request, "password", "1234");

        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.username").exists())
        .andExpect(jsonPath("$.email").exists())
        .andExpect(jsonPath("$.password").exists())
        .andExpect(jsonPath("$.username").value(request.getUsername()))
        .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @DisplayName("등록된 계정으로 로그인을 한다.")
    @Test
    public void loginIsOk() throws Exception {
        UserLoginDto request = new UserLoginDto("test email", "1234");

        mockMvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.email").exists())
        .andExpect(jsonPath("$.password").exists())
        .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @DisplayName("유저 정보를 가져올 때 토큰은 필수로 한다.")
    @Test
    public void getCurrentUserIs401() throws Exception {
        mockMvc.perform(
                get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(401));
    }

    @WithMockUser(username = "test email", roles = "USER", password = "1234")
    @DisplayName("유저 정보를 가져온다.")
    @Test
    public void getCurrentUserIsOk() throws Exception {
        String token = jwtProvider.createToken("test email");

        mockMvc.perform(
                get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
        ).andExpect(status().isOk());
    }

    @DisplayName("유저 정보를 변경할 때 토큰은 필수로 한다.")
    @Test
    public void changeUserInfoIs401() throws Exception {
        mockMvc.perform(
                put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(401));
    }

    @WithMockUser(username = "test email", roles = "USER", password = "1234")
    @DisplayName("유저 정보를 변경한다.")
    @Test
    public void changeUserInfoIsOk() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        ReflectionTestUtils.setField(request, "email", "test email");
        ReflectionTestUtils.setField(request, "username", "테스트");
        ReflectionTestUtils.setField(request, "password", "4321");
        ReflectionTestUtils.setField(request, "bio", "change bio");
        ReflectionTestUtils.setField(request, "image", "change image");

        String token = jwtProvider.createToken("test email");

        mockMvc.perform(
                put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }
}