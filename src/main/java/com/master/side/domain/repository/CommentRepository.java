package com.master.side.domain.repository;

import com.master.side.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 추가 쿼리 메서드 선언 가능
}
