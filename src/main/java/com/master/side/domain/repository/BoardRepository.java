package com.master.side.domain.repository;

import com.master.side.domain.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {

    // deleted = false인 데이터를 createdAt 내림차순으로 정렬
    Page<Board> findAllByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

}


