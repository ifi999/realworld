package ifi.realworld.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtProviderTest {

    private final String secret;
    private final Key key;
    private final long validityMilliSeconds;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public JwtProviderTest(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.validity-in-seconds}") long validityMilliSeconds) {
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.validityMilliSeconds = validityMilliSeconds * 1000;
    }

    @DisplayName("토큰 생성")
    @Test
    public void createJwt() {
        //given
        String jws = getJws();

        //when
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws)
                .getBody();

        //then
        assertThat(claims.get("email")).isEqualTo("test@rw.com");
        assertThat(claims.get("username")).isEqualTo("h1");
    }

    @DisplayName("토큰 검증")
    @Test
    public void validateToken() {
        //given
        String jws = getJws();

        //when
        boolean validateToken = validateTokenClaims(jws);

        //then
        assertThat(validateToken).isTrue();
    }

    private String getJws() {
        Date expiration = new Date(System.currentTimeMillis() + this.validityMilliSeconds);

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "test@rw.com"); // 실제로는 HttpServletRequest에서 값 가져와야함
        claims.put("username", "h1");

        String jws = Jwts.builder()
//                .setSubject("test@rw.com")
                .setHeader(headerMap)
                .setClaims(claims)
                .signWith(key, signatureAlgorithm)
                .setExpiration(expiration)
                .compact();
        return jws;
    }

    private boolean validateTokenClaims(String jws) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jws)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            // TODO - JwtException 종류 및 처리 알아보기
        } catch (ClaimJwtException e) {
            //
        } catch (UnsupportedJwtException e) {
            //
        }

        return false;
    }

}