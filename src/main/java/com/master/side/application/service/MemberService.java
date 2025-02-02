package com.master.side.application.service;

import com.master.side.domain.model.Member;
import com.master.side.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

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
}
