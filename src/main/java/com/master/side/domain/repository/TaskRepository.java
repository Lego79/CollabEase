package com.master.side.domain.repository;

import com.master.side.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    // 추가 쿼리 메서드 선언 가능
}
