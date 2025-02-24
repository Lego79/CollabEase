package com.master.side.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.side.application.dto.BoardDto;
import com.master.side.application.dto.CreateBoardRequest;
import com.master.side.application.dto.UpdateBoardRequest;
import com.master.side.application.service.BoardService;
import com.master.side.common.constant.ErrorCode;
import com.master.side.common.exception.CustomException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDto> createBoard(
            @RequestPart("boardData") String boardData,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        // JSON 문자열을 POJO로 변환
        CreateBoardRequest request;
        try {
            request = new ObjectMapper().readValue(boardData, CreateBoardRequest.class);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

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
