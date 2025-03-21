package com.master.side.domain.repository;

import com.master.side.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByBoardIdAndDeletedFalse(UUID boardId);
}
