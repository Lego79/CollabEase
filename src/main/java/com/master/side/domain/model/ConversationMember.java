package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversation_members", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ConversationMember {

    @EmbeddedId
    private ConversationMemberId id;

    // 복합 키의 일부를 conversation, member로 매핑
    @MapsId("conversationId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false)
    private Conversation conversation;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    public ConversationMember() {
    }

    public ConversationMember(Conversation conversation, Member member) {
        this.conversation = conversation;
        this.member = member;
        this.id = new ConversationMemberId(
                conversation.getId(),
                member.getMemberId()
        );
    }

    public ConversationMemberId getId() {
        return id;
    }

    public void setId(ConversationMemberId id) {
        this.id = id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
