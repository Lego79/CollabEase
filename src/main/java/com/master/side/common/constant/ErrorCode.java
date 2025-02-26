package com.master.side.common.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자를 생성 (enum 내부 생성자와 동일)
public enum ErrorCode {

    // 예) 본인이 작성한 글이 아닌데 삭제하려 할 경우
    FORBIDDEN_DELETE("FORBIDDEN_BOARD_DELETE", HttpStatus.FORBIDDEN, "본인이 작성한 글만 삭제할 수 있습니다."),

    // 예) 게시글을 찾지 못한 경우
    NOT_FOUND_BOARD("NOT_FOUND_BOARD", HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),

    // 예) 댓글, 작업 등등 계속 추가...
    FORBIDDEN_COMMENT_DELETE("FORBIDDEN_COMMENT_DELETE", HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 삭제할 수 있습니다."),
    NOT_FOUND_COMMENT("NOT_FOUND_COMMENT", HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),

    TASK_NOT_SELECTED("TASK_NOT_SELECTED", HttpStatus.BAD_REQUEST, "Task를 선택하셔야 합니다."),
    FORBIDDEN_BOARD_DELETE("FORBIDDEN_BOARD_DELETE", HttpStatus.BAD_REQUEST, "본인이 작성한 게시글만 삭제할 수 있습니다."),
    FORBIDDEN_BOARD_UPDATE("FORBIDDEN_BOARD_UPDATE", HttpStatus.BAD_REQUEST, "본인이 작성한 게시글만 수정할 수 있습니다."),
    INVALID_REQUEST("TASK_NOT_SELECTED", HttpStatus.BAD_REQUEST, "Task를 선택하셔야 합니다.");

    private final String code;         // 에러 식별 코드 (내부 로깅 또는 클라이언트 에러 핸들링 시 사용)
    private final HttpStatus status;   // HTTP 상태 코드
    private final String message;      // 사용자에게 보여줄 메시지
}
