package ifi.realworld.user.api;

import ifi.realworld.user.app.service.UserService;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    EntityManager em;

    @Autowired
    UserPasswordEncoder passwordEncoder;

    // TODO - 테스트 뭔가 잘못된듯.. service, dto 사용해야함
    @DisplayName("유저 등록")
    @Test
    public void createUser() {
        //given
        User user = userBuilder();

        //when
        User result = userRepository.save(user);

        //then
        assertThat(user.getEmail()).isEqualTo(result.getEmail());
        assertThat(user.getUsername()).isEqualTo(result.getUsername());
    }

    private User userBuilder() {
        return User.builder()
                .email("test@rw.com")
                .username("hi")
                .password("1234")
                .passwordEncoder(passwordEncoder)
                .build();
    }

    @DisplayName("로그인")
    @Test
    public void login() {
        //given
        User user = setUser();
        em.flush();
        em.clear();


        //when
//        User login = userService.login(dto);

        //then
//        assertThat(user.getUsername()).isEqualTo(login.getUsername());

    }

    private User setUser() {
        return userRepository.save(userBuilder());
    }

}