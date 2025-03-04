package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversations", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id") // DB의 기본 키 컬럼과 일치시킴
    private Long id;

    // 대화 참여자 (간단하게 사용자명 문자열로 저장; 실제로는 User 엔티티를 참조하도록 구현 가능)
    private String user1Username;
    private String user2Username;

    private LocalDateTime createdAt = LocalDateTime.now();
}
