package com.master.side.security;

import com.master.side.domain.model.Member;
import com.master.side.domain.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    // Inject your MemberRepository (or equivalent) to fetch member data.
    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // This method loads the member by username and wraps it in CustomUserDetails.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found: " + username));
        return new CustomUserDetails(member);
    }
}
