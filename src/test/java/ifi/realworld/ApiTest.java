package ifi.realworld;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ApiTest {

    @Value("${jwt.secret}")
    private String secret;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    protected Cookie JWT_COOKIE;

    @BeforeEach
    void setUp() {
        JWT_COOKIE = new Cookie("Authorization", createToken());
        JWT_COOKIE.setMaxAge(1800 * 1000);
        JWT_COOKIE.setPath("/");
    }

    public String createToken() {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1800 * 1000);

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "test@hello.com");

        return Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)), signatureAlgorithm)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .compact();
    }

}
