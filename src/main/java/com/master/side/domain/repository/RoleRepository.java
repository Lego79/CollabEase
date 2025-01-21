package com.master.side.domain.repository;

import com.master.side.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // 추가 쿼리 메서드 선언 가능
}
