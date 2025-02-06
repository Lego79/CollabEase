package com.master.side.application.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private UUID id;
    private String content;
    private Instant createdAt;
    private String commenterUsername;

}
