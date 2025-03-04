package com.master.side.application.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {
    private UUID memberId;       // 메시지 보낸 사람의 ID (보내는 사람)
    private String userNickname; // 메시지 보낸 사람의 닉네임
    private String content;      // 메시지 내용
    private String timestamp;    // 메시지 전송 시간 (ISO-8601 형식)
    private String roomId;       // 어느 채팅방에서 보냈는지 식별 (ex. 개인 채팅인 경우 sender, receiver 조합)
}
