package com.master.side.domain.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many Notifications belong to one Member.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_notification_member"))
    private Member member;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "message", nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    public Notification() {
    }

    // Getters and setters
    // ... (omitted for brevity)
}