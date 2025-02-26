package com.master.side.application.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBoardRequest {
    private UUID boardId;
    private String title;
    private String content;
}