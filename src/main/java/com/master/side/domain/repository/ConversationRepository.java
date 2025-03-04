package com.master.side.domain.repository;

import com.master.side.domain.model.Conversation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends CrudRepository<Conversation, Long> {

    // 특정 두 사용자 간의 대화방 존재 여부 확인 (user1-user2 순서와 user2-user1 순서를 모두 확인)
    @Query("SELECT c FROM Conversation c WHERE " +
            "(c.user1Username = :user1 AND c.user2Username = :user2) OR " +
            "(c.user1Username = :user2 AND c.user2Username = :user1)")
    Optional<Conversation> findByParticipants(@Param("user1") String user1, @Param("user2") String user2);

    // 사용자가 참여자로 있는 모든 대화방 조회
    List<Conversation> findByUser1UsernameOrUser2Username(String user1, String user2);
}
