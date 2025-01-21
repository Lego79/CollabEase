package com.master.side.domain.repository;

import com.master.side.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 추가 쿼리 메서드 선언 가능
}
