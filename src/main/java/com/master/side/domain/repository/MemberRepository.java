package com.master.side.domain.repository;

import com.master.side.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 추가 쿼리 메서드 선언 가능
}
