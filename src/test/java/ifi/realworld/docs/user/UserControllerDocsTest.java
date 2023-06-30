package ifi.realworld.docs.user;

import ifi.realworld.docs.RestDocsSupport;
import ifi.realworld.user.api.UserController;
import ifi.realworld.user.api.dto.*;
import ifi.realworld.user.app.service.UserService;
import ifi.realworld.user.domain.User;
import ifi.realworld.utils.security.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = Mockito.mock(UserService.class);

    private final JwtProvider jwtProvider = Mockito.mock(JwtProvider.class);

    @Override
    protected Object initController() {
        return new UserController(userService);
    }

    User user = User.builder()
            .email("test email")
            .password("1234")
            .passwordEncoder(userPasswordEncoder)
            .username("테스트")
            .build();

    @DisplayName("신규 유저 등록 API")
    @Test
    public void createUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        ReflectionTestUtils.setField(request, "username", "test2 user");
        ReflectionTestUtils.setField(request, "email", "test2@hello.com");
        ReflectionTestUtils.setField(request, "password", "1234");

        User createUserEntity = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .passwordEncoder(userPasswordEncoder)
                .username(request.getUsername())
                .build();

        System.out.println("createUserEntity.getPassword() = " + createUserEntity.getPassword());

        UserCreateResponse userCreateResponse = UserCreateResponse.of(createUserEntity);

        given(userService.createUser(any(UserCreateRequest.class)))
                .willReturn(userCreateResponse);

        mockMvc.perform(
                        post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "create-user"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , requestFields(
                        fieldWithPath("username").type(STRING)
                                .description("사용자 이름")
                        , fieldWithPath("email").type(STRING)
                                .description("사용자 이메일")
                        , fieldWithPath("password").type(STRING)
                                .description("비밀번호 원본")
                )
                , responseFields(
                        fieldWithPath("username").type(STRING)
                                .description("사용자 이름")
                        , fieldWithPath("email").type(STRING)
                                .description("사용자 이메일")
                        , fieldWithPath("password").type(STRING)
                                .optional()
                                .description("암호화 비밀번호")
                )
        ));
    }

    @DisplayName("로그인 API")
    @Test
    public void login() throws Exception {
        UserLoginDto request = new UserLoginDto("test email", "1234");

        given(userService.login(any(UserLoginDto.class), any(HttpServletResponse.class)))
                .willReturn(request);

        mockMvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "login"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , requestFields(
                        fieldWithPath("email").type(STRING)
                                .description("사용자 이메일")
                        , fieldWithPath("password").type(STRING)
                                .description("비밀번호")
                )
                , responseFields(
                        fieldWithPath("email").type(STRING)
                                .description("사용자 이메일")
                        , fieldWithPath("password").type(STRING)
                                .optional()
                                .description("비밀번호")
                )
        ));
    }

    @DisplayName("유저 정보 조회 API")
    @Test
    public void getCurrentUser() throws Exception {
        UserInfoDto userInfoDto = UserInfoDto.of(user);

        given(userService.getCurrentUserInfo())
                .willReturn(userInfoDto);

        mockMvc.perform(
                get("/api/users")
        ).andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "get-current-user"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , responseFields(
                        fieldWithPath("email").type(STRING)
                                .description("사용자 이메일")
                        , fieldWithPath("username").type(STRING)
                                .description("사용자 이름")
                        , fieldWithPath("bio").type(STRING)
                                .optional()
                                .description("사용자 소개")
                        , fieldWithPath("image").type(STRING)
                                .optional()
                                .description("사용자 프로필 사진")
                )
        ));
    }

    @DisplayName("유저 정보 수정 API")
    @Test
    public void changeUserInfo() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        ReflectionTestUtils.setField(request, "email", "test email");
        ReflectionTestUtils.setField(request, "username", "테스트");
        ReflectionTestUtils.setField(request, "password", "4321");
        ReflectionTestUtils.setField(request, "bio", "change bio");
        ReflectionTestUtils.setField(request, "image", "change image");

        UserInfoDto userInfoDto = UserInfoDto.of(user);

        given(userService.changeUserInfo(any(UserUpdateRequest.class), any(HttpServletResponse.class)))
                .willReturn(userInfoDto);

        mockMvc.perform(
                put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "change-user-info"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , requestFields(
                        fieldWithPath("email").type(STRING)
                                .description("사용자 이메일")
                        , fieldWithPath("username").type(STRING)
                                .description("사용자 이름")
                        , fieldWithPath("password").type(STRING)
                                .description("비밀번호")
                        , fieldWithPath("bio").type(STRING)
                                .description("사용자 소개")
                        , fieldWithPath("image").type(STRING)
                                .description("사용자 프로필 사진")
                )
                , responseFields(
                        fieldWithPath("email").type(STRING)
                                .description("사용자 이메일")
                        , fieldWithPath("username").type(STRING)
                                .description("사용자 이름")
                        , fieldWithPath("bio").type(STRING)
                                .optional()
                                .description("사용자 소개")
                        , fieldWithPath("image").type(STRING)
                                .optional()
                                .description("사용자 프로필 사진")
                )
        ));
    }
}