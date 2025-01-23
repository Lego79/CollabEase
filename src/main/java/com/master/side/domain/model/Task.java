package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "task")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many Tasks belong to one Member.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_member"))
    private Member member;

    // Many Tasks belong to one Board.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_board"))
    private Board board;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(nullable = false, length = 50, columnDefinition = "varchar(50) default 'PENDING'")
    private String status = "PENDING";

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    public Task() {
    }

    // Getters and setters
    // ... (omitted for brevity)
}