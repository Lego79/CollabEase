package com.master.side.security;

import com.master.side.application.service.MemberService;
import com.master.side.domain.model.Member;
import com.master.side.security.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, MemberService memberService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        log.info("OAuth2 로그인 성공 핸들러 진입");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        log.info("OAuth2 사용자 - email: {}, name: {}", email, name);

        // 데이터베이스에서 회원을 조회하거나 신규 등록
        Member member = memberService.processOAuthPostLogin(email, name);
        log.info("Member 처리 완료: {}", member);

        // JWT 토큰 생성 (여기서는 access token만 생성)
        String token = jwtUtil.generateToken(member.getEmail());
        log.info("JWT 토큰 생성: {}", token);

        // 프론트엔드의 /board 페이지로 리다이렉트하면서, 토큰을 쿼리 파라미터로 전달
        String redirectUrl = "http://localhost:8888/oauth-redirect?token=" + token;
        log.info("프론트엔드로 리다이렉트: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
