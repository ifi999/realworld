package ifi.realworld.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifi.realworld.ApiTest;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserLoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    public void createUser_IsOk() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        ReflectionTestUtils.setField(request, "username", "테스트");
        ReflectionTestUtils.setField(request, "email", "test2@hello.com");
        ReflectionTestUtils.setField(request, "password", "1234");

        mvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.username").exists())
        .andExpect(jsonPath("$.username").value(request.getUsername()));
    }

    @Test
    public void login_IsOk() throws Exception {
        UserLoginDto request = new UserLoginDto("test@hello.com", "1234");

        // TODO - isOK는 되는데, andExpect(jsonPath("$.username").exists()) 같은 것들은 검증이 필요없는지?
        //        어자피 없는 데이터일거긴하니 필요가 업는건가?
        mvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    public void getUser_Is401() throws Exception {
        mvc.perform(
                get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(401));
    }

    @Test
    public void getUser_IsOk() throws Exception {
        mvc.perform(
                get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.JWT_COOKIE)
        ).andExpect(status().isOk());
    }

    @Test
    public void updateUser_Is401() throws Exception {
        mvc.perform(
                put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(401));
    }

    @Test
    public void updateUser_IsOk() throws Exception {
        mvc.perform(
                put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.JWT_COOKIE)
        ).andExpect(status().isOk());
    }
}