package com.master.side.domain.repository;

import com.master.side.domain.model.MemberRole;
import com.master.side.domain.model.MemberRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, MemberRoleId> {
    // 추가 쿼리 메서드 선언 가능
}
