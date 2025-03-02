package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 메시지가 속한 채팅방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_chat_message_room"))
    private ChatRoom chatRoom;

    // 보낸 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_chat_message_sender"))
    private Member sender;

    // 메시지 내용
    @Column(nullable = false, columnDefinition = "text")
    private String content;

    // 생성, 수정 시점
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    // soft delete
    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;

    // 읽음 여부
    @Column(name = "is_read", nullable = false, columnDefinition = "boolean default false")
    private Boolean isRead;
}
