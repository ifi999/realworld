package ifi.realworld.user.domain;

import ifi.realworld.utils.security.UserPasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserTest {

    @Autowired
    private UserPasswordEncoder userPasswordEncoder;

    @DisplayName("비밀번호 검증에 성공한다.")
    @Test
    public void isMatchedWithSuccess() {
        // given
        User user = User.builder()
                .email("test email")
                .username("테스트")
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .build();
        String encodedPassword = userPasswordEncoder.encode("1234");

        // when
        boolean isMatched = user.isMatched("1234", encodedPassword, userPasswordEncoder);

        // then
        assertThat(isMatched).isTrue();
    }

    @DisplayName("비밀번호 검증에 실패한다.")
    @Test
    public void isMatched() {
        // given
        User user = User.builder()
                .email("test email")
                .username("테스트")
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .build();
        String encodedPassword = userPasswordEncoder.encode("1234");

        // when
        boolean isMatched = user.isMatched("4321", encodedPassword, userPasswordEncoder);

        // then
        assertThat(isMatched).isFalse();
    }

    @DisplayName("유저 정보를 변경한다.")
    @Test
    public void changeInfo() {
        // given
        User user = User.builder()
                .email("test email")
                .username("테스트")
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .build();

        // when
        user.changeInfo(
                "updated name", "updated email"
                , "1111", userPasswordEncoder
                , "updated bio", "updated image");

        // then
        assertThat(user.getEmail()).isEqualTo("updated email");
        assertThat(user.getUsername()).isEqualTo("updated name");
        assertThat(user.getBio()).isEqualTo("updated bio");
        assertThat(user.getImage()).isEqualTo("updated image");
    }

    @DisplayName("passwordEncoder가 null이라면 유저 정보 변경에 실패한다.")
    @Test
    public void changeInfoWithPasswordEncoderIsNull() {
        // given
        User user = User.builder()
                .email("test email")
                .username("테스트")
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .build();

        // then
        assertThatThrownBy(() ->
                user.changeInfo(
                        "updated name", "updated email"
                        , "1111", null
                        , "updated bio", "updated image"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("passwordEncoder is null");
    }

}