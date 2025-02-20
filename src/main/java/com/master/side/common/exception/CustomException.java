package com.master.side.common.exception;

import com.master.side.common.constant.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        // 부모 생성자에 ErrorCode의 메시지를 전달합니다.
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
