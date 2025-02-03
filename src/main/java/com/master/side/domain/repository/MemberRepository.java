package com.master.side.domain.repository;

import com.master.side.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);


}