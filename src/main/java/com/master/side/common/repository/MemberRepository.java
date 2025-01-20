package com.master.side.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.master.side.common.entitiy.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
}