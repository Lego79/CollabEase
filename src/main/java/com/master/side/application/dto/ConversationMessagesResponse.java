package com.master.side.application.dto;

import lombok.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationMessagesResponse {
    private Long conversationId;
    private List<String> participants; // 예: [user1, user2]
    private List<MessageResponse> messages;
    private Pageable pageable;
    private long totalElements;
}
