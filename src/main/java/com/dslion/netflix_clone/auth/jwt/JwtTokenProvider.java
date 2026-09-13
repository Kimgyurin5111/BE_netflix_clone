package com.dslion.netflix_clone.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// 토큰 발급&검증 역할만 하는 클래스
@Component
public class JwtTokenProvider {

    // application-local.yaml의 jwt.secret, jwt.access-token-expiration-ms 값을 그대로 읽어온다
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 로그인 성공 시 토큰 발급
    public String createAccessToken(Long userId, String email, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getKey())
                .compact();
    }

    // 토큰 안에 들어있는 정보(claims) 꺼내기
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰이 유효한지(위조, 만료되지 않았는지) 확인
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
