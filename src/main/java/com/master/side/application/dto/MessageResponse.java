package com.master.side.application.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * ChatMessage 엔티티를 REST API로 응답할 때 사용
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    private Long messageId;
    private Long conversationId;
    private String senderUsername; // 실제 로그인 시 사용하는 식별자
    private String senderNickname; // 화면에 표시할 별명
    private String content;
    private LocalDateTime timestamp;
}