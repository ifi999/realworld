package ifi.realworld.user.api;

import ifi.realworld.user.domain.repository.UserRepository;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserPasswordEncoder passwordEncoder;

    @DisplayName("유저 등록")
    @Test
    public void createUser() {
        //given
        User user = getUser();

        //when
        User result = userRepository.save(user);

        //then
        assertThat(user.getEmail()).isEqualTo(result.getEmail());
        assertThat(user.getUsername()).isEqualTo(result.getUsername());
    }

    private User getUser() {
        return User.builder()
                .email("test@rw.com")
                .username("hi")
                .password("1234")
                .passwordEncoder(passwordEncoder)
                .build();
    }

}