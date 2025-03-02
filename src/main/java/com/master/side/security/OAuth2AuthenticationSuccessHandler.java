package com.master.side.security;

import com.master.side.application.service.MemberService;
import com.master.side.domain.model.Member;
import com.master.side.security.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2 로그인 성공 시, JwtUtil로 토큰을 생성하고 프론트엔드로 리다이렉트하는 핸들러
 */
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;




    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;

    }
// OAuth2AuthenticationSuccessHandler.java

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        CustomUserDetails userDetails;

        if (principal instanceof CustomUserDetails) {
            userDetails = (CustomUserDetails) principal;
        } else if (principal instanceof org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser) {
            org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser oidcUser =
                    (org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser) principal;
            // OIDC 사용자에서 필요한 정보를 추출합니다.
            String email = oidcUser.getAttribute("email");
            String name = oidcUser.getAttribute("name");

            // MemberService를 통해 해당 이메일과 이름으로 Member를 조회하거나 신규 등록합니다.
            Member member = memberService.processOAuthPostLogin(email, name);
            // Member를 CustomUserDetails로 변환합니다.
            userDetails = new CustomUserDetails(member);
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }

        // 추가 클레임 생성: memberId와 userNickname 정보를 넣습니다.
        Map<String, Object> additionalClaims = new HashMap<>();
        additionalClaims.put("memberId", userDetails.getMemberId());
        additionalClaims.put("userNickname", userDetails.getNickname());
        additionalClaims.put("auth", "ROLE_USER");

        // 새로운 Authentication 객체 생성하여 CustomUserDetails를 담습니다.
        Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());

        // JwtTokenProvider의 createToken 메서드 호출 시, 새 Authentication 객체를 전달합니다.
        String token = jwtTokenProvider.createToken(newAuth, additionalClaims);

        // 프론트엔드로 리다이렉트 (예시: 쿼리 파라미터에 token 포함)
        String redirectUrl = "http://localhost:5173/oauth-redirect?token=" + token;
        response.sendRedirect(redirectUrl);
    }




}
