package com.master.side.domain.repository;

import com.master.side.domain.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 특정 대화방의 메시지를 sentAt 오름차순으로 조회 (리스트)
    // List<ChatMessage> findByConversation_ConversationIdOrderBySentAtAsc(Long conversationId);

    // 특정 대화방의 메시지를 페이징하여 조회 (Conversation의 id 필드를 사용)
    Page<ChatMessage> findByConversation_Id(Long conversationId, Pageable pageable);
}
