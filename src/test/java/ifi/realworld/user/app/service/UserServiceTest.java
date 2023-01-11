package ifi.realworld.user.app.service;

import ifi.realworld.common.exception.UserNotFoundException;
import ifi.realworld.common.security.JwtProvider;
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
    @Autowired
    JwtProvider jwtProvider;

    private User setUser() {
        // TODO - @beforeEach 로 setUser()을 해주고 싶었는데, 값을 못 꺼내오는 것 같았음.. 잘못한건지 다른 방법 있는지 알아보기
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
        Optional<User> findUser = userRepository.findByEmailOrUsername("rw@hel.lo", "회원");

        //then
        assertThat(findUser.isPresent()).isTrue();
    }

    @DisplayName("유저 생성")
    @Test
    public void createUser() {
        //given
        User user = User.builder()
                .username("회원")
                .email("rw@hel.lo")
                .password("1234")
                .passwordEncoder(passwordEncoder)
                .build();

        //when
        User savedUser = userRepository.save(user);

        //then
        assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());
    }

    @DisplayName("로그인")
    @Test
    public void login() {
        //given
        User user = setUser();
        em.flush();
        em.clear();

        //when
        User findUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UserNotFoundException(user.getEmail() + " not found."));

        //then
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }

    @DisplayName("jwt 생성")
    @Test
    public void createJWT() {
        //given
        User user = setUser();

        //when
        String token = jwtProvider.createToken(user.getEmail());

        //then
        assertThat(token).isNotEmpty();
        assertThat(token.length()).isGreaterThan(user.getEmail().length());
    }

    @DisplayName("현재 접속 중인 유저 정보")
    @Test
    public void createCurrentUserInfo() {
        //given
        // TODO - security test 찾아보기 -> MockMvc 공부 필요
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String email = userDetails.getUsername();

        //when
//        userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email + " not found."));

        //then
    }

    @DisplayName("유저 정보 수정")
    @Test
    public void updateUser() {
        //given
        User user = setUser();

        //when
        user.changeInfo(
                "유저이름수정", "email수정",
                null, passwordEncoder,
                null, "이미지수정"
        );

        //then
        assertThat(user.getUsername()).isNotEqualTo("회원");
        assertThat(user.getBio()).isNull();
        assertThat(user.getImage()).isEqualTo("이미지수정");
    }

}