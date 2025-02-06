package com.master.side.application.dto;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {
    private UUID boardId;
    private String content;
    private UUID parentCommentId; // 최상위 댓글의 경우 null

}