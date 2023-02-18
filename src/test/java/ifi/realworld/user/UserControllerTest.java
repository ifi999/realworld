package ifi.realworld.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserLoginDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    public void createUserTest() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        ReflectionTestUtils.setField(request, "username", "테스트");
        ReflectionTestUtils.setField(request, "email", "test@email.com");
        ReflectionTestUtils.setField(request, "password", "1234");

        mvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.username").value(request.getUsername()));
    }

    @Test
    public void loginTest() throws Exception {
        // TODO - 실제 데이터가 아니라, 가짜 데이터로 로그인하도록 변경 필요
        UserLoginDto request = new UserLoginDto("bye@hel.lo", "1234");

        mvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value(request.getEmail()));
    }
}