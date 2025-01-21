package com.master.side.domain.repository;

import com.master.side.domain.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 추가 쿼리 메서드 선언 가능
}
