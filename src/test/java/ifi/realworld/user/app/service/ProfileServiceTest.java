package ifi.realworld.user.app.service;

import ifi.realworld.user.api.dto.ProfileDto;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import ifi.realworld.utils.exception.api.UserNotFoundException;
import ifi.realworld.utils.security.CustomUserDetailsService;
import ifi.realworld.utils.security.UserPasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class ProfileServiceTest {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPasswordEncoder userPasswordEncoder;

    @BeforeEach
    void setUp() {
        User existedUser1 = createUserEntity("test1", "테스트1");
        User existedUser2 = createUserEntity("test2", "테스트2");
        userRepository.saveAll(List.of(existedUser1, existedUser2));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저 프로필을 조회한다.")
    @Test
    public void getProfile() {
        // given
        setUserDetailService("test1");
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
        setUserDetailService("test1");
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
        setUserDetailService("test1");
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
        setUserDetailService("test1");
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
        setUserDetailService("test1");
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
        setUserDetailService("test1");
        String targetUsername = "테스트3";

        // then
        assertThatThrownBy(() -> profileService.unFollowUser(targetUsername))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(targetUsername + " not found.");
    }

    private void setUserDetailService(String email) {
        CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
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