package com.master.side.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * 간단하게 이메일만 포함한 JWT를 생성 및 검증하는 유틸 클래스 (소셜 로그인용)
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secretBase64;

    private final long jwtExpirationMs = 86400000;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("[JwtUtil] HS512 Key initialized (length = {} bits)", keyBytes.length * 8);
    }

    /**
     * 이메일과 함께 "auth" 클레임(기본 ROLE_USER)을 포함한 JWT 생성
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("auth", "ROLE_USER")  // 기본 권한 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 토큰에서 이메일(Subject) 추출
     */
    public String getEmailFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateJwt(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("[validateJwt] JWT 만료됨: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("[validateJwt] 지원되지 않는 JWT: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("[validateJwt] 잘못된 JWT: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("[validateJwt] JWT 서명 오류: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("[validateJwt] JWT 클레임이 비어 있음: {}", e.getMessage());
        }
        return false;
    }
}
