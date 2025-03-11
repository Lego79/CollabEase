package com.master.side.presentation.controller;

import com.master.side.application.dto.ConversationMessagesResponse;
import com.master.side.application.dto.ConversationRequest;
import com.master.side.application.dto.ConversationResponse;
import com.master.side.application.service.ChatService;
import com.master.side.application.service.ConversationService;
import com.master.side.security.util.SecurityContextHelper;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ConversationService conversationService;


    public ChatController(ChatService chatService, ConversationService conversationService) {
        this.chatService = chatService;
        this.conversationService = conversationService;
    }

    /**
     * WebSocket STOMP 엔드포인트 - 실시간 채팅 메시지 수신
     * 클라이언트가 /app/chat-ws/{conversationId}로 메시지를 보내면 처리됨
     */
    @MessageMapping("/chat-ws/{conversationId}")
    public void receiveMessage(@DestinationVariable("conversationId") Long conversationId,
                               @Payload String content,
                               SimpMessageHeaderAccessor headerAccessor) {
        // headerAccessor에서 사용자 정보를 가져옴
        Principal principal = headerAccessor.getUser();
        if (principal == null) {
            throw new RuntimeException("No authenticated user found in STOMP header");
        }
        String currentUsername = principal.getName();
        System.out.println("currentUsername = " + currentUsername);

        chatService.handleIncomingMessage(conversationId, currentUsername, content);
    }



    /**
     * REST API: 현재 사용자의 모든 대화방 목록 조회
     */
    @GetMapping("/conversations")
    public List<ConversationResponse> listConversations(Principal principal) {
        String currentUsername = principal.getName();
        return conversationService.getConversationsForUser(currentUsername);
    }

    @PostMapping("/conversations")
    public ConversationResponse createConversation(Principal principal,
                                                   @RequestBody ConversationRequest request) {
        String currentUsername = SecurityContextHelper.getCurrentUsername();
        String targetUsername = request.getParticipantUsername();
        return conversationService.createOrGetConversation(currentUsername, targetUsername);
    }

    /**
     * 대화방 메시지를 그룹핑하여 반환
     */
    @GetMapping("/conversations/{conversationId}/grouped-messages")
    public ConversationMessagesResponse getGroupedMessages(@PathVariable("conversationId") Long conversationId,
                                                           @RequestParam(name = "page", defaultValue = "1") int page,
                                                           @RequestParam(name = "size", defaultValue = "20") int size) {
        return chatService.getConversationMessagesGrouped(conversationId, page, size);
    }

    //채팅착 삭제, 수정, 조회 API 추가
    //게시글 삭제, 수정, 조회 API 추가

}