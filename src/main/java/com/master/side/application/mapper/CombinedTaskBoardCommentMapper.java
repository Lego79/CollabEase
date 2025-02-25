package com.master.side.application.mapper;

import com.master.side.application.dto.CombinedTaskBoardCommentResponse;
import com.master.side.application.dto.TaskResponseDto;
import com.master.side.domain.model.Board;
import com.master.side.domain.model.Comment;
import com.master.side.domain.model.FilesEntity;
import com.master.side.domain.model.Task;

import java.util.Collections;

public class CombinedTaskBoardCommentMapper {

    public static TaskResponseDto toTaskResponseDto(Task task) {
        return TaskResponseDto.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .startDate(task.getStartDate().toLocalDateTime())
                .endDate(task.getEndDate() != null ? task.getEndDate().toLocalDateTime() : null)
                .status(task.getStatus())
                .createdAt(task.getCreatedAt().toLocalDateTime())
                .updatedAt(task.getUpdatedAt().toLocalDateTime())
                .build();
    }

    public static CombinedTaskBoardCommentResponse toCombinedDto(Board board) {
        // Task 정보
        Task task = board.getTask();
        // Board 작성자 정보 (Board의 member를 기준)
        var member = board.getMember();

        return CombinedTaskBoardCommentResponse.builder()
                // === Task 정보 ===
                .taskId(task.getId())
                .taskTitle(task.getTitle())
                .taskDescription(task.getDescription())
                .taskStartDate(task.getStartDate().toLocalDateTime())
                .taskEndDate(task.getEndDate() != null ? task.getEndDate().toLocalDateTime() : null)
                .taskStatus(task.getStatus())
                .taskCreatedAt(task.getCreatedAt().toLocalDateTime())
                .taskUpdatedAt(task.getUpdatedAt().toLocalDateTime())
                // === Board 정보 ===
                .boardId(board.getId())
                .boardTitle(board.getTitle())
                .boardContent(board.getContent())
                .boardViewCount(board.getViewCount())
                .boardCreatedAt(board.getCreatedAt().toLocalDateTime())
                .boardUpdatedAt(board.getUpdatedAt().toLocalDateTime())
                // === Comment 정보 ===
                .comments(board.getComments().stream()
                        .map(CombinedTaskBoardCommentMapper::toCommentDto)
                        .toList())
                // === 작성자 정보 ===
                .username(member.getUsername())
                .nickname(member.getNickname())
                // === 파일 정보 추가 ===
                .files(board.getFiles() != null ? board.getFiles().stream()
                        .map(CombinedTaskBoardCommentMapper::toFileDto)
                        .toList() : Collections.emptyList())
                .build();
    }

    public static CombinedTaskBoardCommentResponse.CommentDto toCommentDto(Comment comment) {
        return CombinedTaskBoardCommentResponse.CommentDto.builder()
                .commentId(comment.getId())
                .commentContent(comment.getContent())
                .commenterUsername(comment.getMember().getUsername())
                .commenterNickname(comment.getMember().getNickname())
                .commentCreatedAt(comment.getCreatedAt().toLocalDateTime())
                .replies(comment.getReplies().stream()
                        .map(CombinedTaskBoardCommentMapper::toCommentDto)
                        .toList())
                .build();
    }

    public static CombinedTaskBoardCommentResponse.FileDto toFileDto(FilesEntity file) {
        return CombinedTaskBoardCommentResponse.FileDto.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                .fileUrl(file.getFileUrl())
                .size(file.getSize())
                .contentType(file.getContentType())
                .createdAt(file.getCreatedAt().toLocalDateTime())
                .updatedAt(file.getUpdatedAt().toLocalDateTime())
                .build();
    }
}
