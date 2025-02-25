package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Many Boards → One Member
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_board_member"))
    private Member member;

    // Many Boards → One Task (추가된 필드)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_board_task"))
    private Task task;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "view_count", columnDefinition = "int4 default 0")
    private Integer viewCount;

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createdAt;

    // 수정 시점을 자동으로 세팅 (Hibernate)
    @Column(name = "updated_at")
    @org.hibernate.annotations.UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "is_deleted")
    private boolean deleted;

    // 1:N with Task (Board가 하위이므로, 반대쪽 매핑은 Task 엔티티에서 관리)
    // 1:N with Comment
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // 1:N with Attachment
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<FilesEntity> files;

}
