package com.master.side.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CombinedTaskBoardCommentDto {
    // Task 정보
    private UUID taskId;
    private String taskTitle;
    private String taskDescription;
    private LocalDateTime taskStartDate;
    private LocalDateTime taskEndDate;
    private String taskStatus;
    private LocalDateTime taskCreatedAt;
    private LocalDateTime taskUpdatedAt;

    // Board 정보
    private UUID boardId;
    private String boardTitle;
    private String boardContent;
    private Integer boardViewCount;
    private LocalDateTime boardCreatedAt;
    private LocalDateTime boardUpdatedAt;

    // Comment 정보
    private List<CommentDto> comments;

    // User 정보
    private String username;
    private String nickname;

    // Inner DTO for Comment
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class CommentDto {
        private UUID commentId;
        private String commentContent;
        private String commenterUsername;
        private LocalDateTime commentCreatedAt;
        private List<CommentDto> replies;
    }
}
