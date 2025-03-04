package com.master.side.domain.repository;

import com.master.side.domain.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    // 특정 대화방의 메시지들을 페이징 조회
    Page<Message> findByConversation_Id(Long conversationId, Pageable pageable);
    // (또는 메서드 이름에 OrderBy를 포함할 수도 있음: findByConversation_IdOrderByTimestampAsc 등)
}