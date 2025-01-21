package com.master.side.domain.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_url", nullable = false, length = 255)
    private String fileUrl;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "uploaded_at", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp uploadedAt;

    // Many Attachments might belong to a Board.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "fk_attachment_board"))
    private Board board;

    // Many Attachments might belong to a Comment.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "fk_attachment_comment"))
    private Comment comment;

    public Attachment() {
    }

    // Getters and setters
    // ... (omitted for brevity)
}