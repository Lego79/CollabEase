package com.master.side.presentation.controller;

import com.master.side.application.dto.BoardDto;
import com.master.side.application.dto.CreateBoardRequest;
import com.master.side.application.dto.UpdateBoardRequest;
import com.master.side.application.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<BoardDto> createBoard(@RequestBody CreateBoardRequest request) {
        BoardDto createdBoard = boardService.createBoard(request);

        // 새로 생성된 Board의 URI를 생성 (예: /api/board/{boardId})
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{boardId}")
                .buildAndExpand(createdBoard.getId()) // DTO에 boardId가 포함되어 있다고 가정
                .toUri();

        return ResponseEntity.created(location).body(createdBoard);
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
