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

    @PostMapping
    public Comment createComment(
            @RequestParam UUID boardId,
            @RequestParam String content
    ) {
        // Service에서 SecurityContextHelper를 통해 User 식별자를 가져옴
        return commentService.createComment(boardId, content);
    }

    @DeleteMapping("/{commentId}")
    public void softDeleteComment(@PathVariable UUID commentId) {
        commentService.softDeleteComment(commentId);
    }
}