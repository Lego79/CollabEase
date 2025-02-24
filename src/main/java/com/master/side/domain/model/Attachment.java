package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "attachment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Board와의 연관관계 (1:N)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_attachment_board"))
    private Board board;

    // 외부 접근 가능한 URL (예: 클라우드 스토리지 혹은 로컬 정적 경로)
    @Column(name = "file_url", nullable = false, length = 255)
    private String fileUrl;

    // 원본 파일명
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    // 실제 파일이 저장된 경로 (로컬 혹은 스토리지 내부 경로)
    @Column(name = "file_path", nullable = false, length = 255)
    private String filePath;

    // 파일 크기를 바이트 단위로 저장 (bigint에 해당)
    @Column(name = "file_size")
    private Long fileSize;

    // 파일 생성 시각 (자동 기록)
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
}
