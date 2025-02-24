package com.master.side.application.service;

import com.master.side.application.dto.BoardDto;
import com.master.side.application.dto.CreateBoardRequest;
import com.master.side.application.dto.UpdateBoardRequest;
import com.master.side.domain.model.Board;
import com.master.side.domain.model.Member;
import com.master.side.domain.model.Task;
import com.master.side.domain.repository.BoardRepository;
import com.master.side.domain.repository.MemberRepository;
import com.master.side.domain.repository.TaskRepository;
import com.master.side.security.util.SecurityContextHelper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final FileStorageService fileStorageService;

    public BoardService(BoardRepository boardRepository,
                        TaskRepository taskRepository,
                        MemberRepository memberRepository,
                        FileStorageService fileStorageService) {
        this.boardRepository = boardRepository;
        this.taskRepository = taskRepository;
        this.memberRepository = memberRepository;
        this.fileStorageService = fileStorageService;
    }
    public BoardDto createBoard(CreateBoardRequest request, List<MultipartFile> files) {
        // 현재 로그인한 사용자 조회
        UUID currentUserId = SecurityContextHelper.getCurrentUserId();
        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 요청에 포함된 Task ID로 Task 엔티티 조회
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        // Board 엔티티 생성 및 저장
        Board board = Board.builder()
                .title(request.getTitle())
                .member(member)
                .content(request.getContent())
                .task(task)
                .deleted(false)
                .build();
        boardRepository.save(board);

        // 파일 첨부가 있다면 FileStorageService를 통해 저장 처리 (DB에 Files 엔티티 기록)
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                fileStorageService.storeFile(file, board, member);
            }
        }

        return BoardDto.fromEntity(board);
    }

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

    public void softDeleteBoard(UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        board.setDeleted(true);
        board.setUpdatedAt(Timestamp.from(Instant.now()));
        boardRepository.save(board);
    }

    public List<BoardDto> getAllBoards() {
        List<Board> boards = boardRepository.findAllByDeletedFalse();
        return boards.stream()
                .map(this::mapToDto)
                .toList();
    }

    private BoardDto mapToDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .deleted(board.isDeleted())
                .build();
    }
}
