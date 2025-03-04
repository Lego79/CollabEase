package com.master.side.application.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * 대화방 생성 시, 필요한 정보
 * - 예: 여러 참여자의 UUID 리스트
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateConversationRequest {
    private List<UUID> memberIds;
}