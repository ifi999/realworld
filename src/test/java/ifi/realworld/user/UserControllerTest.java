package ifi.realworld.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifi.realworld.user.api.dto.UserCreateRequest;
import ifi.realworld.user.api.dto.UserLoginDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Value("${jwt.secret}")
    String secret;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

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

    @Test
    public void getUserTest() throws Exception {
        Cookie cookie = new Cookie("Authorization", createToken());
        cookie.setMaxAge(1800 * 1000);
        cookie.setPath("/");

        mvc.perform(
                get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").exists());
    }

    private String createToken() {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1800 * 1000);

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "bye@hel.lo");

        return Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)), signatureAlgorithm)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .compact();
    }
}