package com.master.side.presentation.controller;

import com.master.side.application.dto.CommentRequestDto;
import com.master.side.application.dto.CommentResponseDto;
import com.master.side.application.mapper.CommentMapper;
import com.master.side.application.service.CommentService;
import com.master.side.common.constant.ErrorCode;
import com.master.side.common.exception.CustomException;
import com.master.side.domain.model.Comment;
import com.master.side.security.util.SecurityContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

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
        // 현재 사용자 ID 가져오기
        UUID currentUserId = SecurityContextHelper.getCurrentUserId();

        // DB에서 댓글 조회
        Comment comment = commentService.findCommentById(commentId);
        if (comment == null) {
            // 댓글을 찾을 수 없으면 예외 발생
            throw new CustomException(ErrorCode.NOT_FOUND_COMMENT);
        }

        log.info("댓글 작성자 ID: {}, 현재 사용자 ID: {}", comment.getMember().getMemberId(), currentUserId);

        // 댓글 작성자와 현재 사용자 비교
        if (!comment.getMember().getMemberId().equals(currentUserId)) {
            // 본인 댓글이 아니라면 FORBIDDEN
            throw new CustomException(ErrorCode.FORBIDDEN_COMMENT_DELETE);
        }

        // 본인 댓글인 경우 soft delete 수행
        commentService.softDeleteComment(commentId);

        // 성공 시 No Content 반환
        return ResponseEntity.noContent().build();
    }
}

