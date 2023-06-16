package ifi.realworld.user.domain.repository;

import ifi.realworld.user.domain.User;
import ifi.realworld.utils.security.CustomUserPasswordEncoder;
import ifi.realworld.utils.security.UserPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserPasswordEncoder userPasswordEncoder;

    @BeforeEach
    void setUp() {
        this.userPasswordEncoder = new CustomUserPasswordEncoder(new BCryptPasswordEncoder());

        User createdUser = createUser();
        userRepository.save(createdUser);
    }

    @DisplayName("이메일과 이름으로 유저를 조회한다.")
    @Test
    public void findByEmailAndUsername() {
        // given
        String targetEmail = "test email";
        String targetUsername = "테스트";

        // when
        Optional<User> user = userRepository.findByEmailAndUsername(targetEmail, targetUsername);

        // then
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo(targetEmail);
        assertThat(user.get().getUsername()).isEqualTo(targetUsername);
    }

    @DisplayName("이메일로 유저를 조회한다.")
    @Test
    public void findByEmail() {
        // given
        String targetEmail = "test email";

        // when
        Optional<User> user = userRepository.findByEmail(targetEmail);

        // then
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo(targetEmail);
    }

    @DisplayName("사용자 이름으로 유저를 조회한다.")
    @Test
    public void findByUsername() {
        // given
        String targetUsername = "테스트";

        // when
        Optional<User> user = userRepository.findByUsername(targetUsername);

        // then
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(targetUsername);
    }

    private User createUser() {
        return User.builder()
                .email("test email")
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .username("테스트")
                .build();
    }

}