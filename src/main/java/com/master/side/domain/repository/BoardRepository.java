package com.master.side.domain.repository;

import com.master.side.domain.model.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {

    // [EntityGraph 적용] deleted = false 조건의 모든 Board와
    // 관련된 Task, Member, Comments(대댓글 포함)까지 한 번에 로딩
    @EntityGraph(attributePaths = {
            "task",
            "task.member",
            "comments",
            "comments.member",
            "comments.replies"
    })
    List<Board> findAllByDeletedFalse();

    // [EntityGraph + @Query 적용] 특정 Task ID에 해당하는 Board 조회 시
    // 연관 엔티티들도 함께 로딩
    @EntityGraph(attributePaths = {
            "task",
            "task.member",
            "comments",
            "comments.member",
            "comments.replies"
    })
    @Query("SELECT b FROM Board b WHERE b.task.id = :taskId AND b.deleted = false")
    List<Board> findByTaskId(@Param("taskId") UUID taskId);

}
