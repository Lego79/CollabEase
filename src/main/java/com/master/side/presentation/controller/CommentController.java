package com.master.side.presentation.controller;

import com.master.side.application.dto.CommentRequestDto;
import com.master.side.application.dto.CommentResponseDto;
import com.master.side.application.mapper.CommentMapper;
import com.master.side.application.service.CommentService;
import com.master.side.domain.model.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    // 생성자 주입
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto) {
        Comment createdComment;
        if (requestDto.getParentCommentId() != null) {
            createdComment = commentService.createComment(
                    requestDto.getBoardId(),
                    requestDto.getContent(),
                    requestDto.getParentCommentId()
            );
        } else {
            createdComment = commentService.createComment(
                    requestDto.getBoardId(),
                    requestDto.getContent()
            );
        }

        CommentResponseDto responseDto = CommentMapper.toResponseDto(createdComment);

        // 새로 생성된 댓글의 URI 생성
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdComment.getId())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> softDeleteComment(@PathVariable("commentId") UUID commentId) {
        commentService.softDeleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

