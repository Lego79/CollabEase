package com.master.side.presentation.controller;

import com.master.side.application.dto.BoardDto;
import com.master.side.application.dto.CreateBoardRequest;
import com.master.side.application.dto.UpdateBoardRequest;
import com.master.side.application.service.BoardService;
import com.master.side.security.CheckCurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // CREATE
    @PostMapping
    @CheckCurrentUser  // 이 어노테이션이 붙으면, AOP Aspect에서 현재 사용자 검증 후 메서드가 실행됩니다.
    public ResponseEntity<BoardDto> createBoard(@RequestBody CreateBoardRequest request) {
        BoardDto board = boardService.createBoard(request);
        return ResponseEntity.ok(board);
    }

    // UPDATE
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardDto> updateBoard(@PathVariable UUID boardId,
                                                @RequestBody UpdateBoardRequest request) {
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
