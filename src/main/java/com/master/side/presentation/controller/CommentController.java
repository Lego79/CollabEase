package com.master.side.presentation.controller;

import com.master.side.application.service.CommentService;
import com.master.side.domain.model.Comment;
import com.master.side.domain.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // CREATE
    @PostMapping
    public Comment createComment(
            @RequestParam UUID boardId,
            @RequestParam String content
    ) {
        // 로그인 Member는 보통 SecurityContextHolder 등으로 가져옴 (아래는 예시)
        Member member = new Member();
        member.setMemberId(UUID.randomUUID()); // 실제 로직 대체

        return commentService.createComment(boardId, member, content);
    }

    // SOFT DELETE
    @DeleteMapping("/{commentId}")
    public void softDeleteComment(@PathVariable UUID commentId) {
        commentService.softDeleteComment(commentId);
    }
}