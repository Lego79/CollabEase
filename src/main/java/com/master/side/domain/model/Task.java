package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;
@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Many Tasks belong to one Member
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_task_member"))
    private Member member;


    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(nullable = false, length = 50,
            columnDefinition = "varchar(50) default 'PENDING'")
    private String status = "PENDING";

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createdAt;

    // 수정 시점을 자동으로 세팅 (Hibernate)
    @Column(name = "updated_at")
    @org.hibernate.annotations.UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}