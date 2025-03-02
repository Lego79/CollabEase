package com.master.side.domain.repository;

import com.master.side.domain.model.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, UUID> {
}
