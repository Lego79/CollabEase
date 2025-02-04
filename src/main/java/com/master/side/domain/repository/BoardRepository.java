package com.master.side.domain.repository;

import com.master.side.domain.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    List<Board> findAllByDeletedFalse();
    @Query("SELECT b FROM Board b WHERE b.task.id = :taskId")
    List<Board> findByTaskId(@Param("taskId") UUID taskId);

}
