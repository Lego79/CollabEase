package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;    // 연관된 대화방

    // 보낸 사람 (여기서는 사용자명 저장; 실제로는 User 엔티티 참조 가능)
    private String senderUsername;

    private String content;
    private LocalDateTime timestamp = LocalDateTime.now();
}