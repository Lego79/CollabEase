package com.master.side.application.service;

import com.master.side.application.dto.ConversationMessagesResponse;
import com.master.side.application.dto.MessageResponse;
import com.master.side.domain.model.ChatMessage;
import com.master.side.domain.model.Conversation;
import com.master.side.domain.model.Member;
import com.master.side.domain.repository.ChatMessageRepository;
import com.master.side.domain.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(ChatMessageRepository chatMessageRepository,
                       MemberRepository memberRepository,
                       ConversationService conversationService,
                       SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.memberRepository = memberRepository;
        this.conversationService = conversationService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * WebSocket을 통해 수신된 채팅 메시지 처리:
     * - 전달받은 conversationId로 Conversation 엔티티 조회
     * - senderUsername을 사용하여 Member 엔티티 조회
     * - ChatMessage 엔티티를 빌더 패턴으로 생성 후 저장
     * - 저장된 메시지를 MessageResponse DTO로 변환하여, 구독중인 클라이언트에 전송
     */
    @Transactional
    public void handleIncomingMessage(Long conversationId, String senderUsername, String content) {
        // conversationId로 Conversation 엔티티 조회
        Conversation conv = conversationService.getConversationById(conversationId);

        // senderUsername으로 Member 엔티티 조회 (MemberRepository에 findByUsername 필요)
        Member sender = memberRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        // ChatMessage 엔티티 생성 (빌더 사용)
        ChatMessage message = ChatMessage.builder()
                .conversation(conv)
                .sender(sender)
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();

        // 메시지 저장
        ChatMessage saved = chatMessageRepository.save(message);

        // MessageResponse DTO 생성
        MessageResponse messageDto = new MessageResponse(
                saved.getMessageId(),
                conv.getId(),
                sender.getUsername(),    // 로그인 식별자
                sender.getNickname(),    // 화면에 표시할 별명
                saved.getContent(),
                saved.getSentAt()
        );

        // 실시간 구독자에게 전송 (예: /topic/conversations/{conversationId})
        String destination = "/topic/conversations/" + conv.getId();
        messagingTemplate.convertAndSend(destination, messageDto);
    }

    /**
     * 단순 메시지 목록 조회 (페이징 처리)
     */
    @Transactional(readOnly = true)
    public Page<MessageResponse> getMessages(Long conversationId, int page, int size) {
        // 클라이언트는 1부터 넘기므로 0-based index로 변환
        int pageIndex = (page > 0) ? page - 1 : 0;
        // 오름차순 정렬: 오래된 메시지가 위쪽, 최신 메시지가 아래쪽에 오도록 함
        PageRequest pageReq = PageRequest.of(pageIndex, size, Sort.by("sentAt").ascending());
        Page<ChatMessage> messagePage = chatMessageRepository.findByConversation_Id(conversationId, pageReq);

        List<MessageResponse> content = messagePage.getContent().stream()
                .map(msg -> new MessageResponse(
                        msg.getMessageId(),
                        conversationId,
                        msg.getSender().getUsername(),  // sender의 실제 username
                        msg.getSender().getNickname(),    // sender의 닉네임
                        msg.getContent(),
                        msg.getSentAt()))
                .toList();
        return new PageImpl<>(content, messagePage.getPageable(), messagePage.getTotalElements());
    }

    /**
     * 대화방 메시지 이력을 참가자 정보와 함께 그룹핑하여 반환.
     * 두 사용자의 정보를 포함하여, 클라이언트가 메시지 발신자를 쉽게 구분할 수 있도록 함.
     */
    @Transactional(readOnly = true)
    public ConversationMessagesResponse getConversationMessagesGrouped(Long conversationId, int page, int size) {
        // 대화방 엔티티 조회
        Conversation conv = conversationService.getConversationById(conversationId);

        int pageIndex = (page > 0) ? page - 1 : 0;
        // 오름차순 정렬: 오래된 메시지가 먼저 나오도록 함
        PageRequest pageReq = PageRequest.of(pageIndex, size, Sort.by("sentAt").ascending());
        Page<ChatMessage> messagePage = chatMessageRepository.findByConversation_Id(conversationId, pageReq);

        List<MessageResponse> messages = messagePage.getContent().stream()
                .map(msg -> new MessageResponse(
                        msg.getMessageId(),
                        conversationId,
                        msg.getSender().getUsername(),  // 로그인 식별자
                        msg.getSender().getNickname(),    // 표시용 별명
                        msg.getContent(),
                        msg.getSentAt()))
                .toList();

        return ConversationMessagesResponse.builder()
                .conversationId(conversationId)
                .participants(Arrays.asList(conv.getUser1Username(), conv.getUser2Username()))
                .messages(messages)
                .pageable(messagePage.getPageable())
                .totalElements(messagePage.getTotalElements())
                .build();
    }
}
