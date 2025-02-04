package com.master.side.application.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBoardRequest {
    private String title;
    private UUID taskId;
    private String content;
}
