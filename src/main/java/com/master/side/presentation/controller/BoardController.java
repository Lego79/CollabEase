package com.master.side.presentation.controller;

import com.master.side.application.dto.BoardDto;
import com.master.side.application.dto.CreateBoardRequest;
import com.master.side.application.dto.UpdateBoardRequest;
import com.master.side.application.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // CREATE
    @PostMapping
    public BoardDto createBoard(@RequestBody CreateBoardRequest request) {
        // 이제 컨트롤러에서는 SecurityContextHelper를 직접 호출하지 않고
        // Service 레이어에서 처리하도록 위임
        return boardService.createBoard(request);
    }

    // UPDATE
    @PutMapping("/{boardId}")
    public BoardDto updateBoard(@PathVariable UUID boardId,
                                @RequestBody UpdateBoardRequest request) {
        return boardService.updateBoard(boardId, request);
    }

    // SOFT DELETE
    @DeleteMapping("/{boardId}")
    public void softDeleteBoard(@PathVariable UUID boardId) {
        boardService.softDeleteBoard(boardId);
    }

    // READ (목록)
    @GetMapping
    public List<BoardDto> getAllBoards() {
        return boardService.getAllBoards();
    }
}
