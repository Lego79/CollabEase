package com.master.side.application.mapper;

import com.master.side.application.dto.CombinedTaskBoardCommentDto;
import com.master.side.application.dto.TaskResponseDto;
import com.master.side.domain.model.Board;
import com.master.side.domain.model.Comment;
import com.master.side.domain.model.Task;

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

    public static CombinedTaskBoardCommentDto toCombinedDto(Board board) {
        Task task = board.getTask();
        // Member 정보는 task에서 가져온다고 가정
        var member = task.getMember();

        return CombinedTaskBoardCommentDto.builder()
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
                .build();
    }

    public static CombinedTaskBoardCommentDto.CommentDto toCommentDto(Comment comment) {
        return CombinedTaskBoardCommentDto.CommentDto.builder()
                .commentId(comment.getId())
                .commentContent(comment.getContent())
                .commenterUsername(comment.getMember().getUsername())
                .commentCreatedAt(comment.getCreatedAt().toLocalDateTime())
                .replies(comment.getReplies().stream()
                        .map(CombinedTaskBoardCommentMapper::toCommentDto)
                        .toList())
                .build();
    }
}
