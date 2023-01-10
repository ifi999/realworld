package ifi.realworld.user.domain;

import ifi.realworld.user.api.UserPasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserTest {

    @Autowired
    UserPasswordEncoder userPasswordEncoder;

    @DisplayName("비밀번호 검증")
    @Test
    public void validatePassword() {
        //given
        String encode = userPasswordEncoder.encode("1234");

        //when
        boolean matched = userPasswordEncoder.matches("1234", encode);

        //then
        assertThat(matched).isTrue();
    }

}