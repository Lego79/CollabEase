package com.master.side.application.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse {
    private Long conversationId;
    private List<String> participants;   // 참여자 사용자명 목록 (또는 ID 목록)
    private LocalDateTime createdAt;

}