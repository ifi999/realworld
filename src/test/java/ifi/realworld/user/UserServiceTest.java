package ifi.realworld.user;

import ifi.realworld.user.api.UserPasswordEncoder;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserCreateResponse;
import ifi.realworld.user.app.service.UserService;
import ifi.realworld.user.app.service.UserServiceImpl;
import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPasswordEncoder userPasswordEncoder;

    @Test
    public void createUser_Success_test() {
        //given
        UserCreateRequest request = new UserCreateRequest();
        ReflectionTestUtils.setField(request, "username", "testUser");
        ReflectionTestUtils.setField(request, "email", "test@email.com");
        ReflectionTestUtils.setField(request, "password", "1234");

        //when
        UserCreateResponse response = userService.createUser(request);

        //then
        assertThat(response.getEmail()).isEqualTo(request.getEmail());

    }

}
