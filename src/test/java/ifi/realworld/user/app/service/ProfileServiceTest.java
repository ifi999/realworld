package ifi.realworld.user.app.service;

import ifi.realworld.TestSupport;
import ifi.realworld.user.api.dto.ProfileDto;
import ifi.realworld.user.domain.User;
import ifi.realworld.utils.exception.api.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileServiceTest extends TestSupport {

    @BeforeEach
    void setUp() {
        User existedUser1 = createUserEntity("test1", "테스트1");
        User existedUser2 = createUserEntity("test2", "테스트2");
        userRepository.saveAll(List.of(existedUser1, existedUser2));

        setUserDetailService("test1");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        SecurityContextHolder.clearContext();
    }

    @DisplayName("유저 프로필을 조회한다.")
    @Test
    public void getProfile() {
        // given
        String targetUsername = "테스트2";

        // when
        ProfileDto profile = profileService.getProfile(targetUsername);

        // then
        assertThat(profile).isNotNull();
        assertThat(profile.getUsername()).isEqualTo("테스트2");
    }

    @DisplayName("등록되지 않은 유저는 프로필 조회에 실패한다.")
    @Test
    public void getProfileWithNotExistedUser() {
        // given
        String targetUsername = "테스트3";

        // then
        assertThatThrownBy(() -> profileService.getProfile(targetUsername))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(targetUsername + " not found.");
    }

    @DisplayName("유저를 팔로우한다.")
    @Test
    public void followUser() {
        // given
        String targetUsername = "테스트2";

        // when
        ProfileDto followUser = profileService.followUser(targetUsername);

        // then
        assertThat(followUser).isNotNull();
        assertThat(followUser.getUsername()).isEqualTo(targetUsername);
        assertThat(followUser.isFollowing()).isTrue();
    }

    @DisplayName("등록되지 않은 유저는 팔로우 할 수 없다.")
    @Test
    public void followUserWithNotExistedUser() {
        // given
        String targetUsername = "테스트3";

        // then
        assertThatThrownBy(() -> profileService.followUser(targetUsername))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(targetUsername + " not found.");
    }

    @DisplayName("팔로우한 유저를 팔로우 취소한다.")
    @Test
    public void unFollowUser() {
        // given
        String targetUsername = "테스트2";

        // when
        ProfileDto followUser = profileService.unFollowUser(targetUsername);

        // then
        assertThat(followUser).isNotNull();
        assertThat(followUser.getUsername()).isEqualTo(targetUsername);
        assertThat(followUser.isFollowing()).isFalse();
    }

    @DisplayName("등록되지 않은 유저는 팔로우 취소 할 수 없다.")
    @Test
    public void unFollowUserWithNotExistedUser() {
        // given
        String targetUsername = "테스트3";

        // then
        assertThatThrownBy(() -> profileService.unFollowUser(targetUsername))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(targetUsername + " not found.");
    }

    private void setUserDetailService(String email) {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Authentication authenticationMock = Mockito.mock(Authentication.class);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getName()).thenReturn(email);

        SecurityContextHolder.setContext(securityContextMock);
    }

    private User createUserEntity(String email, String username) {
        return User.builder()
                .email(email)
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .username(username)
                .build();
    }

}