package com.master.side.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class TaskResponseDto {
    private UUID taskId;
    private UUID memberId;
    private String username;
    private String nickName;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TaskResponseDto(UUID taskId,
                           UUID memberId,
                           String username,
                           String nickName,
                           String title,
                           String description,
                           LocalDateTime startDate,
                           LocalDateTime endDate,
                           String status,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
        this.taskId = taskId;
        this.memberId = memberId;
        this.username = username;
        this.nickName = nickName;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter/Setter(또는 Lombok @Data) 생략
}