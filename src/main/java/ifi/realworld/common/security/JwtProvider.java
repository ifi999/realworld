package ifi.realworld.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {
    // https://github.com/murraco/spring-boot-jwt 참고

    private final String header;
    private final Key key;
    private final long validityMilliSeconds;
    private final CustomUserDetailsService userDetailsService;
    private final SignatureAlgorithm signatureAlgorithm;

    public JwtProvider(
            @Value("${jwt.secret}") String secret
            , @Value("${jwt.header}") String header
            , @Value("${jwt.validity-in-seconds}") long validityMilliSeconds
            , CustomUserDetailsService userDetailsService) {
        this.header = header;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.validityMilliSeconds = validityMilliSeconds * 1000L;
        this.userDetailsService = userDetailsService;
        this.signatureAlgorithm = SignatureAlgorithm.HS256;
    }

    public String createToken(String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + this.validityMilliSeconds);

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        return Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .signWith(key, signatureAlgorithm)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList());
    }

    private String getEmail(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email");
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(header);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            // TODO - JwtException 종류 및 처리 알아보기
            log.info("Expired JWT.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT.");
        } catch (ClaimJwtException e) {
            log.info("Not Expected JWT claims.");
        } catch (IllegalArgumentException e) {
            log.info("IllegalArgument JWT.");
        }

        return false;
    }

}
