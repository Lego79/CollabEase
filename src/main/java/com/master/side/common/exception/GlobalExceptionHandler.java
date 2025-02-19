package com.master.side.common.exception;

import com.master.side.common.constant.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        // ErrorResponse: 실제로 클라이언트에게 내려줄 JSON 형태 정의
        ErrorResponse response = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage()
        );
        // errorCode가 가진 HttpStatus를 사용
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    // 필요 시 다른 예외(NullPointerException 등)도 추가 가능
}