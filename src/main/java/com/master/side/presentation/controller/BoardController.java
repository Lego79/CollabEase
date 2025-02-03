package com.master.side.presentation.controller;

import com.master.side.application.dto.BoardDto;
import com.master.side.application.dto.CreateBoardRequest;
import com.master.side.application.dto.UpdateBoardRequest;
import com.master.side.application.service.BoardService;
import com.master.side.domain.model.Member;
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
        // 예시: 로그인된 사용자를 Member로 조회 (간단히 하드코딩)
        // 실제로는 SecurityContextHolder에서 email/username을 꺼낸 뒤 MemberService 등으로 조회
        Member member = new Member();
        member.setMemberId(UUID.randomUUID()); // 실제 로직 대체

        return boardService.createBoard(member, request);
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
