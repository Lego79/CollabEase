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
    private final FileStorageService fileStorageService; // 파일 저장 관련 서비스

    public BoardController(BoardService boardService, FileStorageService fileStorageService) {
        this.boardService = boardService;
        this.fileStorageService = fileStorageService;
    }

    // 파일 업로드 전용 엔드포인트
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
        // 파일 용량, 타입 검증 로직 추가 가능
        String fileUrl = fileStorageService.storeFile(file); // 파일 저장 후 URL 반환
        return ResponseEntity.ok(fileUrl);
    }

    // 기존 게시글 생성 API
    @PostMapping
    public ResponseEntity<BoardDto> createBoard(
            @RequestPart("boardData") CreateBoardRequest request,
            // 첨부파일은 필요시 추가 (혹은 boardData 내에 업로드된 파일 URL을 포함)
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
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
