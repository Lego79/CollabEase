package com.master.side.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Spring Security의 Authentication 객체를 사용하여 토큰을
 * 발급 및 검증하는 클래스
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final CustomUserDetailsService customUserDetailsService;

    // jwt.secret: 64바이트 이상의 Base64 문자열 (HS512용)
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            CustomUserDetailsService customUserDetailsService
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailsService = customUserDetailsService;
        log.info("[JwtTokenProvider] Key length = {} bits", keyBytes.length * 8);
    }

    /**
     * Authentication 객체를 받아 AccessToken과 RefreshToken을 생성
     */
    public JwtToken generateToken(Authentication authentication) {
        String authorities = "ROLE_USER";
        long now = System.currentTimeMillis();

        Date accessTokenExpiresIn = new Date(now + 86400000);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 토큰의 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("[validateToken] Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("[validateToken] Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("[validateToken] Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("[validateToken] JWT claims string is empty.", e);
        }
        return false;
    }

    /**
     * 토큰에서 클레임을 추출하여 Authentication 객체로 변환
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String authString = (String) claims.get("auth");
        if (authString == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        Collection<SimpleGrantedAuthority> authorities =
                Arrays.stream(authString.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        String username = claims.getSubject();
        CustomUserDetails userDetails =
                (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    /**
     * 토큰에서 Claims를 파싱 (만료된 토큰이라도 Claims 반환)
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
