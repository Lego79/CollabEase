package com.master.side.presentation.controller;

import com.master.side.application.dto.CommentRequestDto;
import com.master.side.application.dto.CommentResponseDto;
import com.master.side.application.mapper.CommentMapper;
import com.master.side.application.service.CommentService;
import com.master.side.domain.model.Comment;
import com.master.side.security.util.SecurityContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
@Slf4j
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
        // 토큰(세션)에서 현재 사용자 ID 가져오기
        UUID currentUserId = SecurityContextHelper.getCurrentUserId();

        // DB에서 comment 조회 (댓글 작성자 정보 확인을 위해)
        Comment comment = commentService.findCommentById(commentId);

        // comment에서 찾은 memberId와 현재 memberId 로그 추가
        log.info("댓글 작성자 ID: {}, 현재 사용자 ID: {}", comment.getMember().getMemberId(), currentUserId);

        // 작성자가 현재 사용자와 일치하지 않으면 예외 발생
        if (!comment.getMember().getMemberId().equals(currentUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "본인이 작성한 댓글만 삭제할 수 있습니다."
            );
        }

        // 작성자가 같다면 soft delete 진행
        commentService.softDeleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

