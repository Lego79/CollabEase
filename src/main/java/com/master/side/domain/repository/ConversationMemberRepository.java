package com.master.side.domain.repository;

import com.master.side.domain.model.ConversationMember;
import com.master.side.domain.model.ConversationMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, ConversationMemberId> {
}
