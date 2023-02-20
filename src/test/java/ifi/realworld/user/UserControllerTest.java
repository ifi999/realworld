package ifi.realworld.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifi.realworld.ApiTest;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserLoginDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class UserControllerTest extends ApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    public void createUserTest() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        ReflectionTestUtils.setField(request, "username", "테스트");
        ReflectionTestUtils.setField(request, "email", "test2@hello.com");
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
        UserLoginDto request = new UserLoginDto("test@hello.com", "1234");

        // TODO - isOK는 되는데, andExpect(jsonPath("$.username").exists()) 같은 것들은 검증이 필요없는지?
        //        어자피 없는 데이터일거긴하니 필요가 업는건가?
        mvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getUserTest() throws Exception {
        mvc.perform(
                get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.JWT_COOKIE)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserTest() throws Exception {
        mvc.perform(
                put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.JWT_COOKIE)
        )
                .andExpect(status().isOk());
    }
}