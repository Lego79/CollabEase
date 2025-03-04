package com.master.side.application.service;

import com.master.side.application.dto.ConversationResponse;
import com.master.side.domain.model.Conversation;
import com.master.side.domain.repository.ConversationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepo;

    public ConversationService(ConversationRepository conversationRepo) {
        this.conversationRepo = conversationRepo;
    }

    /**
     * 사용자별 대화방 목록 조회
     */
    @Transactional
    public List<ConversationResponse> getConversationsForUser(String username) {
        List<Conversation> conversations = conversationRepo.findByUser1UsernameOrUser2Username(username, username);
        // Conversation 엔티티를 DTO로 변환
        return conversations.stream()
                .map(conv -> new ConversationResponse(
                        conv.getId(),
                        Arrays.asList(conv.getUser1Username(), conv.getUser2Username()),
                        conv.getCreatedAt()))
                .toList();
    }

    /**
     * 두 사용자의 대화방을 찾거나 없으면 생성
     */
    @Transactional
    public ConversationResponse createOrGetConversation(String userA, String userB) {
        if (userB == null || userB.trim().isEmpty()) {
            throw new IllegalArgumentException("대화 상대의 username이 제공되지 않았습니다.");
        }
        Optional<Conversation> existing = conversationRepo.findByParticipants(userA, userB);
        Conversation conversation;
        if (existing.isPresent()) {
            conversation = existing.get();
        } else {
            conversation = Conversation.builder()
                    .user1Username(userA)
                    .user2Username(userB)
                    .build();
            conversation = conversationRepo.save(conversation);
        }
        return new ConversationResponse(
                conversation.getId(),
                Arrays.asList(conversation.getUser1Username(), conversation.getUser2Username()),
                conversation.getCreatedAt());
    }


    @Transactional
    public Conversation getConversationById(Long conversationId) {
        return conversationRepo.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found with id: " + conversationId));
    }
}
