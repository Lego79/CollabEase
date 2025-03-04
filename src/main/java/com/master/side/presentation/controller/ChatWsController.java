//// ChatWsController.java
//package com.master.side.presentation.controller;
//
//import com.master.side.application.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.stereotype.Controller;
//
//import java.security.Principal;
//
//@Controller  // STOMP 메시지용이므로 @Controller 사용 (@RestController 사용 시에도 동작 가능)
//@RequiredArgsConstructor
//public class ChatWsController {
//    private final ChatService chatService;
//
//    // 4. WebSocket을 통한 메시지 수신 처리 -> 구독중인 Topic으로 전송
//    @MessageMapping("/chat/{conversationId}")
//    public void sendMessageStomp(@DestinationVariable Long conversationId,
//                                 ChatMessageDto messageDto,
//                                 Principal principal) {
//        // principal에서 현재 사용자 ID를 가져온다 (Jwt 인증 시 username 또는 ID 설정함)
//        Long senderId = Long.parseLong(principal.getName());
//        // 서비스 이용하여 메시지 저장 및 전송 (구독중인 클라이언트에게 전달)
//        chatService.sendMessage(senderId, conversationId, messageDto.getContent());
//        // 참고: SimpMessagingTemplate을 직접 사용할 경우, ChatService 내부에서 호출하므로 여기서는 반환 불필요
//    }
//}
