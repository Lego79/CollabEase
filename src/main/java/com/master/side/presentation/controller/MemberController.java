package com.master.side.presentation.controller;

import com.master.side.application.dto.MemberResponseDto;
import com.master.side.application.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/chat-member")
    public ResponseEntity<List<MemberResponseDto>> findMemberForChat(
            @RequestParam("username") String username) {
        List<MemberResponseDto> result = memberService.searchChatMembers(username);
        return ResponseEntity.ok(result);
    }
}
