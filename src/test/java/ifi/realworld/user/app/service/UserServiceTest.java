package ifi.realworld.user.app.service;

import ifi.realworld.user.api.UserPasswordEncoder;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Autowired
    UserPasswordEncoder passwordEncoder;

    // TODO - @beforeEach 로 setUser()을 해주고 싶었는데, 값을 못 꺼내오는 것 같았음.. 잘못한건지 다른 방법 있는지 알아보기

    private User setUser() {
        User user1 = User.builder()
                .username("회원")
                .email("rw@hel.lo")
                .password("1234")
                .passwordEncoder(passwordEncoder)
                .build();
        return userRepository.save(user1);
    }

    @DisplayName("유저 중복 체크")
    @Test
    public void checkDuplicatedUser() {
        //given
        setUser();
        em.flush();
        em.clear();

        //when
        Optional<User> findUser = userRepository.findByEmailAndUsername("rw@hel.lo", "회원");

        //then
        assertThat(findUser.isPresent()).isTrue();
    }

    @DisplayName("비밀번호 검증")
    @Test
    public void checkPassword() {
        //given
        User user = setUser();

        //when // TODO - 이건 Entity 테스트인지?
        boolean matchedTrue = user.isMatched("1234", user.getPassword(), passwordEncoder);
        boolean matchedFalse = user.isMatched("4321", user.getPassword(), passwordEncoder);

        //then
        assertThat(matchedTrue).isTrue();
        assertThat(matchedFalse).isFalse();
    }

    @DisplayName("로그인")
    @Test
    public void login() {
        //given
        User user = setUser();
        em.flush();
        em.clear();

        //when
        User findUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        //then
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }

}