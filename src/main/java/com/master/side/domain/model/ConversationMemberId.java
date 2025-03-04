package com.master.side.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class ConversationMemberId implements Serializable {

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "member_id")
    private UUID memberId;

    public ConversationMemberId() {
    }

    public ConversationMemberId(Long conversationId, UUID memberId) {
        this.conversationId = conversationId;
        this.memberId = memberId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    // equals, hashCode 반드시 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConversationMemberId)) return false;
        ConversationMemberId that = (ConversationMemberId) o;
        return Objects.equals(conversationId, that.conversationId) &&
                Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversationId, memberId);
    }
}
