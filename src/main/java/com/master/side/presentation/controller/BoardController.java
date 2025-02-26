package com.master.side.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.side.application.dto.BoardDto;
import com.master.side.application.dto.CreateBoardRequest;
import com.master.side.application.dto.UpdateBoardRequest;
import com.master.side.application.service.BoardService;
import com.master.side.application.service.FileStorageService;
import com.master.side.common.constant.ErrorCode;
import com.master.side.common.exception.CustomException;
import com.master.side.domain.model.Board;
import com.master.side.domain.repository.BoardRepository;
import com.master.side.security.util.SecurityContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final FileStorageService fileStorageService;
    private final BoardRepository boardRepository; // 작성자 검증용으로 Repository 주입

    public BoardController(BoardService boardService,
                           FileStorageService fileStorageService,
                           BoardRepository boardRepository) {
        this.boardService = boardService;
        this.fileStorageService = fileStorageService;
        this.boardRepository = boardRepository;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDto> createBoard(
            @RequestPart("boardData") String boardData,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        // JSON 문자열 -> CreateBoardRequest
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

    /**
     * “POST /api/board/update” → 게시글 수정
     * 멱등성(REST 원칙)과는 어긋나지만,
     * 내부 정책상 POST를 사용한다고 가정한 예시.
     */
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> updateBoard(@RequestBody UpdateBoardRequest request) {
        // 1) DB에서 해당 게시글 조회 (작성자 검증)
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOARD));

        // 2) 현재 로그인 사용자 식별자 획득
        UUID currentUserId = SecurityContextHelper.getCurrentUserId();

        // 3) 게시글 작성자와 현재 사용자가 일치하는지 검증
        if (!board.getMember().getMemberId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_BOARD_UPDATE);
        }

        // 4) 제목과 내용만 업데이트 수행 (파일 업데이트는 없음)
        BoardDto updatedBoard = boardService.updateBoard(request, null);
        return ResponseEntity.ok(updatedBoard);
    }




    @PostMapping("/delete")
    public ResponseEntity<Void> softDeleteBoard(@RequestParam("boardId") UUID boardId) {
        // DB 조회 + 작성자 검증 로직 (필요하다면 같은 형태로 추가)
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOARD));

        UUID currentUserId = SecurityContextHelper.getCurrentUserId();
        if (!board.getMember().getMemberId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_BOARD_DELETE);
        }

        boardService.softDeleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }



    /**
     * 파일 다운로드
     */
    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileUrl") String fileUrl) throws IOException {
        Resource resource = fileStorageService.downloadFile(fileUrl);
        if (resource == null || !resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(resource.getFile().toPath());
        if (!StringUtils.hasText(contentType)) {
            contentType = "application/octet-stream";
        }
        String filename = resource.getFilename();
        if (!StringUtils.hasText(filename)) {
            filename = "unknown_file";
        }
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
