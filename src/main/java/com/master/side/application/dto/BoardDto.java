package com.master.side.application.dto;

import com.master.side.domain.model.Board;
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
    private boolean deleted;
    // 정적 메서드로 정의
    public static BoardDto fromEntity(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .deleted(board.isDeleted())
                .build();
    }}