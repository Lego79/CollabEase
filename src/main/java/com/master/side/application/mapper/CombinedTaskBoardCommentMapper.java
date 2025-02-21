package com.master.side.application.mapper;

import com.master.side.application.dto.CombinedTaskBoardCommentResponse;
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

    public static CombinedTaskBoardCommentResponse toCombinedDto(Board board) {
        Task task = board.getTask();
        // Board에 있는 실제 작성자 정보를 사용합니다.
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
                // === 작성자 정보는 Board의 member를 기준으로 합니다. ===
                .username(member.getUsername())
                .nickname(member.getNickname())
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
}
