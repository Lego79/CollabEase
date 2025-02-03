package com.master.side.application.service;

import com.master.side.application.dto.BoardDto;
import com.master.side.application.dto.CreateBoardRequest;
import com.master.side.application.dto.UpdateBoardRequest;
import com.master.side.domain.model.Board;
import com.master.side.domain.model.Member;
import com.master.side.domain.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // CREATE
    public BoardDto createBoard(Member member, CreateBoardRequest request) {
        Board board = Board.builder()
                .id(UUID.randomUUID())
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .viewCount(0)
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .isDeleted(false)
                .build();
        Board saved = boardRepository.save(board);
        return mapToDto(saved);
    }

    // UPDATE
    public BoardDto updateBoard(UUID boardId, UpdateBoardRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        if (board.isDeleted()) {
            throw new RuntimeException("Board is already deleted");
        }

        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setUpdatedAt(Timestamp.from(Instant.now()));
        Board updated = boardRepository.save(board);

        return mapToDto(updated);
    }

    // SOFT DELETE
    public void softDeleteBoard(UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        board.setDeleted(true);
        board.setUpdatedAt(Timestamp.from(Instant.now()));
        boardRepository.save(board);
    }

    // READ (목록)
    public List<BoardDto> getAllBoards() {
        List<Board> boards = boardRepository.findAllByIsDeletedFalse();
        return boards.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // 매핑 메서드
    private BoardDto mapToDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .isDeleted(board.isDeleted())
                .build();
    }
}