//package com.master.side.configuration.member.controller;
//
//import com.master.side.configuration.member.dto.SignInDto;
//import com.master.side.configuration.member.service.MemberService;
//import com.master.side.security.JwtToken;
//import com.master.side.security.util.SecurityUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/members")
//public class MemberController {
//
//    private final MemberService memberService;
//
//    @PostMapping("/sign-in")
//    public JwtToken signIn(@RequestBody SignInDto signInDto) {
//        String username = signInDto.getUsername();
//        String password = signInDto.getPassword();
//        JwtToken jwtToken = memberService.signIn(username, password);
//        log.info("request username = {}, password = {}", username, password);
//        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
//        return jwtToken;
//    }
//
//    @PostMapping("/test")
//    public String test() {
//        return SecurityUtil.getCurrentUsername();
//    }
//}