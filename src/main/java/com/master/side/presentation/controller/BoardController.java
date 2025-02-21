package com.master.side.presentation.controller;

import com.master.side.application.dto.BoardDto;
import com.master.side.application.dto.CreateBoardRequest;
import com.master.side.application.dto.UpdateBoardRequest;
import com.master.side.application.service.BoardService;
import com.master.side.common.constant.ErrorCode;
import com.master.side.common.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    // 생성자 주입
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // CREATE (게시글 + 파일을 한 번에 업로드)
    @PostMapping
    public ResponseEntity<BoardDto> createBoard(
            // JSON으로 들어오는 게시글 정보
            @RequestPart("boardData") CreateBoardRequest request,
            // 첨부파일은 필수가 아니므로 required = false
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        // task가 선택되지 않은 경우 예외 발생
        if (request.getTaskId() == null) {
            throw new CustomException(ErrorCode.TASK_NOT_SELECTED);
        }
        BoardDto board = boardService.createBoard(request, files);
        return ResponseEntity.ok(board);
    }

    // UPDATE
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardDto> updateBoard(
            @PathVariable UUID boardId,
            @RequestBody UpdateBoardRequest request
    ) {
        BoardDto updatedBoard = boardService.updateBoard(boardId, request);
        return ResponseEntity.ok(updatedBoard);
    }

    // SOFT DELETE
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> softDeleteBoard(@PathVariable UUID boardId) {
        boardService.softDeleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    // READ (목록 조회)
    @GetMapping
    public ResponseEntity<List<BoardDto>> getAllBoards() {
        List<BoardDto> boards = boardService.getAllBoards();
        return ResponseEntity.ok(boards);
    }
}
