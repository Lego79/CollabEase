package com.master.side.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CombinedTaskBoardCommentDto {

    // === Task 정보 ===
    private UUID taskId;
    private String taskTitle;
    private String taskDescription;
    private LocalDateTime taskStartDate;
    private LocalDateTime taskEndDate;
    private String taskStatus;
    private LocalDateTime taskCreatedAt;
    private LocalDateTime taskUpdatedAt;

    // === Board 정보 (게시글 정보) ===
    private UUID boardId;
    private String boardTitle;
    private String boardContent;
    private Integer boardViewCount;
    private LocalDateTime boardCreatedAt;
    private LocalDateTime boardUpdatedAt;

    // === Comment 정보 ===
    private List<CommentDto> comments;

    // === 작성자 정보 (Task 작성자) ===
    private String username;
    private String nickname;

    /**
     * Comment(댓글) 정보를 담는 내부 DTO
     * 대댓글은 replies 필드에 재귀적으로 저장
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentDto {
        private UUID commentId;
        private String commentContent;
        private String commenterUsername;
        private String commenterNickname;
        private LocalDateTime commentCreatedAt;
        private List<CommentDto> replies;
    }
}
