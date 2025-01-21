package com.master.side.domain.repository;

import com.master.side.domain.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    // 추가 쿼리 메서드 선언 가능
}
