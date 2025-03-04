package com.master.side.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationRequest {
    private String participantUsername;  // 대화 상대방의 사용자명 (혹은 ID)

    public String getParticipantUsername() {
        return participantUsername;
    }
    public void setParticipantUsername(String participantUsername) {
        this.participantUsername = participantUsername;
    }
}