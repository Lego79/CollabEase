package com.master.side.presentation.controller;

import com.master.side.application.dto.MessageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // 클라이언트가 "/app/chat"으로 보낸 메시지를 처리
    @MessageMapping("/chat")
    @SendTo("/topic/chat")  // 구독 중인 모든 클라이언트에게 broadcasting
    public MessageRequest broadcastMessage(@Payload MessageRequest message) {
        // 필요 시 message 가공 또는 저장 로직 추가 가능
        return message;  // 메시지를 그대로 반환하면 /topic/chat 구독자들에게 전달됨
    }
}

