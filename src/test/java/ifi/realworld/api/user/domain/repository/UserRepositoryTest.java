package ifi.realworld.api.user.domain.repository;

import ifi.realworld.api.TestSupport;
import ifi.realworld.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends TestSupport {

    @BeforeEach
    void setUp() {
        User createdUser = createUserEntity();
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

    private User createUserEntity() {
        return User.builder()
                .email("test email")
                .password("1234")
                .passwordEncoder(userPasswordEncoder)
                .username("테스트")
                .build();
    }

}