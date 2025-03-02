// src/main/java/com/master/side/config/SecurityConfig.java
package com.master.side.infrastructure.securityConfig;

import com.master.side.security.JwtAuthenticationFilter;
import com.master.side.security.JwtTokenProvider;
import com.master.side.security.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {

    private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final JwtTokenProvider jwtTokenProvider; // 토큰Provider도 컨테이너에서 주입

    // 생성자 주입
    public SecurityConfig(
            OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // (1) SecurityFilterChain 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/login/**", "/oauth2/**", "/ws-stomp/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((req, res, authException) -> {
                            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                        })
                )
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(redirection ->
                                redirection.baseUri("/login/oauth/google")
                        )
                        .successHandler(oauth2AuthenticationSuccessHandler)
                );

        // (2) @Bean으로 등록한 jwtAuthenticationFilter()를 필터 체인에 추가
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // (3) JwtAuthenticationFilter를 Bean으로 등록
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}