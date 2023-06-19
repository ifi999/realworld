package ifi.realworld.user.app.service;

import ifi.realworld.TestSupport;
import ifi.realworld.user.api.dto.*;
import ifi.realworld.user.domain.User;
import ifi.realworld.utils.exception.api.AlreadyExistedUserException;
import ifi.realworld.utils.exception.api.InvalidEmailException;
import ifi.realworld.utils.exception.api.PasswordNotMatchedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserServiceTest extends TestSupport {

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        SecurityContextHolder.clearContext();
    }

    @DisplayName("신규 유저를 등록한다.")
    @Test
    public void createUser() {
        //given
        UserCreateRequest request = getUserCreateRequest();

        //when
        UserCreateResponse response = userService.createUser(request);

        //then
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getUsername()).isEqualTo(request.getUsername());
        assertThat(response.getPassword()).isNotEqualTo(request.getPassword());
    }

    @DisplayName("신규 유저 등록 시 이미 등록된 유저면 등록에 실패한다.")
    @Test
    public void createUserWithDuplicatedUser() {
        // given
        User existedUser = createUserEntity();
        userRepository.save(existedUser);

        UserCreateRequest request = getUserCreateRequest();

        // then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(AlreadyExistedUserException.class)
                .hasMessage("This " + request.getEmail() + " already exists.");
    }

    @DisplayName("로그인에 성공한다.")
    @Test
    public void login() {
        // given
        User existedUser = createUserEntity();
        userRepository.save(existedUser);

        UserLoginDto dto = new UserLoginDto("test email", "1234");

        // when
        UserLoginDto loginUser = userService.login(dto, mockHttpServletResponse);

        // then
        assertThat(loginUser.getEmail()).isEqualTo(existedUser.getEmail());
        assertThat(loginUser.getPassword()).isEqualTo(existedUser.getPassword());
    }

    @DisplayName("존재하지 않는 이메일을 입력할 시에는 로그인에 실패한다.")
    @Test
    public void loginWithInvalidEmail() {
        // given
        User existedUser = createUserEntity();
        userRepository.save(existedUser);

        UserLoginDto dto = new UserLoginDto("invalid email", "1234");

        // then
        assertThatThrownBy(() -> userService.login(dto, mockHttpServletResponse))
                .isInstanceOf(InvalidEmailException.class)
                .hasMessage("Invalid " + dto.getEmail() + ".");
    }

    @DisplayName("잘못된 비밀번호를 입력할 시에는 로그인에 실패한다.")
    @Test
    public void loginWithInvalidPassword() {
        // given
        User existedUser = createUserEntity();
        userRepository.save(existedUser);

        UserLoginDto dto = new UserLoginDto("test email", "1111");

        // then
        assertThatThrownBy(() -> userService.login(dto, mockHttpServletResponse))
                .isInstanceOf(PasswordNotMatchedException.class)
                .hasMessage(dto.getEmail());
    }

    @DisplayName("유저 정보 변경에 성공한다.")
    @Test
    public void changeUserInfo() {
        // given
        User existedUser = createUserEntity();
        userRepository.save(existedUser);
        setUserDetailService(existedUser.getEmail());

        UserUpdateRequest dto = UserUpdateRequest.builder()
                .email("updated email")
                .username("updated name")
                .password("1111")
                .image("updated image")
                .bio("updated bio")
                .build();

        // when
        UserInfoDto changeUserInfo = userService.changeUserInfo(dto, mockHttpServletResponse);

        // then
        assertThat(changeUserInfo).isNotEqualTo(existedUser);
        assertThat(changeUserInfo.getEmail()).isEqualTo(dto.getEmail());
        assertThat(changeUserInfo.getUsername()).isEqualTo(dto.getUsername());
        assertThat(changeUserInfo.getImage()).isEqualTo(dto.getImage());
        assertThat(changeUserInfo.getBio()).isEqualTo(dto.getBio());
        assertThat(existedUser.getPassword()).isNotEqualTo(dto.getPassword());
    }

    @DisplayName("이미 등록된 이메일과 유저 이름으로는 유저 정보 변경에 실패한다.")
    @Test
    public void changeUserInfoWithFail() {
        // given
        User existedUser = createUserEntity();
        userRepository.save(existedUser);
        setUserDetailService(existedUser.getEmail());

        UserUpdateRequest dto = UserUpdateRequest.builder()
                .email("test email")
                .username("테스트")
                .build();

        // then
        assertThatThrownBy(() -> userService.changeUserInfo(dto, mockHttpServletResponse))
                .isInstanceOf(AlreadyExistedUserException.class)
                .hasMessage("This Email or Name already exists.");
    }

    private UserCreateRequest getUserCreateRequest() {
        UserCreateRequest request = new UserCreateRequest();
        ReflectionTestUtils.setField(request, "username", "테스트");
        ReflectionTestUtils.setField(request, "email", "test email");
        ReflectionTestUtils.setField(request, "password", "1234");
        return request;
    }

    private User createUserEntity() {
        return User.builder()
                .email("test email")
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .username("테스트")
                .build();
    }

    private void setUserDetailService(String email) {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Authentication authenticationMock = Mockito.mock(Authentication.class);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getName()).thenReturn(email);

        SecurityContextHolder.setContext(securityContextMock);
    }

}
