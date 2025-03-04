package com.master.side.application.service;

import com.master.side.application.dto.MemberResponseDto;
import com.master.side.domain.model.Member;
import com.master.side.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepository;

    public Member processOAuthPostLogin(String email, String name) {
        log.info("MemberService: 이메일로 회원 조회 - {}", email);
        return memberRepository.findByEmail(email).orElseGet(() -> {
            log.info("신규 회원 등록 - email: {}, name: {}", email, name);
            Member newMember = Member.builder()
                    .email(email)
                    .username(email)
                    .nickname(name != null ? name : email)
                    .password("")  // OAuth의 경우 비밀번호는 필요 없음
                    .createdAt(Timestamp.from(Instant.now()))
                    .updatedAt(Timestamp.from(Instant.now()))
                    .build();
            Member savedMember = memberRepository.save(newMember);
            log.info("신규 회원 등록 완료: {}", savedMember);
            return savedMember;
        });
    }

    public List<MemberResponseDto> searchChatMembers(String username) {
        // 검색어 길이가 너무 짧으면 빈 리스트 반환
        if (username == null || username.trim().length() < 2) {
            return Collections.emptyList();
        }

        List<Member> members = memberRepository.findTop20ByUsernameContainingIgnoreCase(username.trim());
        return members.stream()
                .map(member -> new MemberResponseDto(member.getMemberId(), member.getUsername()))
                .collect(Collectors.toList());
    }
}
