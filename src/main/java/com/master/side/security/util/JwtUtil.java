package com.master.side.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    // 만약 application.yml이나 다른 설정 파일에서 주입받고 싶다면 @Value 어노테이션을 활용하세요.
    // 비밀 키가 짧으면 HS512 알고리즘에 대해 안전하지 않으므로, Keys.secretKeyFor(SignatureAlgorithm.HS512)를 사용하는 것이 좋습니다.
    private Key key;
    private final long jwtExpirationMs = 86400000; // 1일

    @PostConstruct
    public void init() {
        // 안전한 HS512용 키 생성 (512비트 이상)
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        // 또는, 고정된 키를 사용하고 싶다면 base64로 인코딩된 512비트 이상의 문자열을 디코딩하여 사용
        // this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode("your-base64-encoded-512-bit-secret"));
        log.info("JWT 비밀 키 생성 완료: {}", key);
    }

    // 이메일(또는 사용자 식별자)를 기반으로 JWT 토큰 생성
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰에서 사용자 이메일 추출
    public String getEmailFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 토큰의 유효성 검증
    public boolean validateJwt(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT 만료됨: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("JWT 서명 오류: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT 클레임이 비어 있음: {}", e.getMessage());
        }
        return false;
    }
}
