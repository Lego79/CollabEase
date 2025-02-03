package com.master.side.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardDto {
    private UUID id;
    private String title;
    private String content;
    private Integer viewCount;
    private boolean isDeleted;
    // 등등 필요한 필드
}