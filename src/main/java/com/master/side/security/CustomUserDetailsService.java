package com.master.side.security;

import com.master.side.domain.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    // Inject your MemberRepository (or equivalent) to fetch member data.
    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // This method loads the member by username and wraps it in CustomUserDetails.
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        try {
            // identifier가 UUID 형식이면 memberId로 조회
            UUID memberId = UUID.fromString(identifier);
            return memberRepository.findById(memberId)
                    .map(CustomUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException("Member not found: " + identifier));
        } catch (IllegalArgumentException e) {
            // UUID 형식이 아니면 기존 username으로 조회
            return memberRepository.findByUsername(identifier)
                    .map(CustomUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException("Member not found: " + identifier));
        }
    }

}
