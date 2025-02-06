package com.master.side.application.service;

import com.master.side.domain.model.Board;
import com.master.side.domain.model.Comment;
import com.master.side.domain.model.Member;
import com.master.side.domain.repository.BoardRepository;
import com.master.side.domain.repository.CommentRepository;
import com.master.side.domain.repository.MemberRepository;
import com.master.side.security.util.SecurityContextHelper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 생성자 주입 방식 (Autowired 어노테이션 대신 생성자 주입을 사용합니다)
    public CommentService(CommentRepository commentRepository,
                          BoardRepository boardRepository,
                          MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    // 최상위 댓글 생성
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
                // 최상위 댓글이므로 parentComment은 null
                .parentComment(null)
                .deleted(false)
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .build();

        return commentRepository.save(comment);
    }

    // 대댓글(댓글의 댓글) 생성
    public Comment createComment(UUID boardId, String content, UUID parentCommentId) {
        // 현재 로그인한 사용자 식별자
        UUID currentUserId = SecurityContextHelper.getCurrentUserId();
        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        if (board.isDeleted()) {
            throw new RuntimeException("Board is deleted");
        }

        // 부모 댓글 조회 (없는 경우 예외 발생)
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        // 대댓글은 부모 댓글이 최상위 댓글인지 검증할 수도 있음.
        // 예: if (parentComment.getParentComment() != null) { throw new RuntimeException("대댓글은 한 단계까지만 허용됩니다."); }

        Comment comment = Comment.builder()
                .id(UUID.randomUUID())
                .board(board)
                .member(member)
                .content(content)
                .parentComment(parentComment) // 부모 댓글 지정
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
