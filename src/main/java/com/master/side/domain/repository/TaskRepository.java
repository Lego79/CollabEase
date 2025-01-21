package com.master.side.domain.repository;

import com.master.side.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // 추가 쿼리 메서드 선언 가능
}
