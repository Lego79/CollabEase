package com.master.side.configuration.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponseDto {
    private String username;
    private String nickname;
    private String address;
}
