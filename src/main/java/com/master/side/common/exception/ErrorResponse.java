package com.master.side.common.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private String code;          // ErrorCode에서 온 식별 코드
    private String message;       // 에러 메시지
    private LocalDateTime timestamp; // 에러 발생 시간

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }


}