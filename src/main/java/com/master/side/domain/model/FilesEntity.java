package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 게시글과의 연관관계 (파일이 어떤 게시글에 첨부되었는지)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_file_board"))
    private Board board;

    // 회원과의 연관관계 (파일 업로드한 사용자를 기록)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_file_member"))
    private Member member;

    @Column(nullable = false, length = 255)
    private String fileName;

    // S3나 로컬 스토리지에 저장된 파일의 URL 또는 경로
    @Column(nullable = false, length = 1024)
    private String fileUrl;

    // 파일 크기를 바이트 단위로 저장
    @Column(nullable = false)
    private Long size;

    // 파일의 MIME 타입 (예: image/png, application/pdf 등)
    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @org.hibernate.annotations.UpdateTimestamp
    private Timestamp updatedAt;
}
