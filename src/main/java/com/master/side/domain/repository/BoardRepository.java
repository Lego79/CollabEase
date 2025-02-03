package com.master.side.domain.repository;

import com.master.side.domain.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    List<Board> findAllByIsDeletedFalse();
}
