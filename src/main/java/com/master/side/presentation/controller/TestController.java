package com.master.side.presentation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        log.info("oauth 로그인 시도 성공");
        System.out.println("oauth 로그인 시도 성공");
        return "Hello, authenticated user!";
    }
}