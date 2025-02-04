package com.master.side.application.service;

import com.master.side.domain.model.Board;
import com.master.side.domain.model.Comment;
import com.master.side.domain.model.Member;
import com.master.side.domain.repository.BoardRepository;
import com.master.side.domain.repository.CommentRepository;
import com.master.side.domain.repository.MemberRepository;
import com.master.side.security.util.SecurityContextHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    public Comment createComment(UUID boardId, String content) {
        // 현재 로그인한 사용자 식별자
        UUID currentUserId = SecurityContextHelper.getCurrentUserId();
        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        if (board.isDeleted()) {
            throw new RuntimeException("Board is deleted");
        }

        Comment comment = Comment.builder()
                .id(UUID.randomUUID())
                .board(board)
                .member(member)
                .content(content)
                .deleted(false)
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .build();

        return commentRepository.save(comment);
    }

    public void softDeleteComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.isDeleted()) {
            comment.setDeleted(true);
            comment.setUpdatedAt(Timestamp.from(Instant.now()));
            commentRepository.save(comment);
        }
    }
}