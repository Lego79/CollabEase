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
        UUID currentUserId = SecurityContextHelper.getCurrentUserId();
        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Board board = Board.builder()
                .title(request.getTitle())
                .member(member)
                .content(request.getContent())
                .task(task)
                .deleted(false)
                .build();
        boardRepository.save(board);

        // 파일 첨부 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                fileStorageService.storeFile(file, board, member);
            }
        }

        return BoardDto.fromEntity(board);
    }

    // ① updateBoard: 이미 컨트롤러에서 작성자 검증 완료. 여기서는 업데이트만 수행
    public BoardDto updateBoard(UpdateBoardRequest request, List<MultipartFile> files) {
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new RuntimeException("Board not found"));

        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setUpdatedAt(Timestamp.from(Instant.now()));

        // 새로운 파일 첨부가 있다면 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                fileStorageService.storeFile(file, board, board.getMember());
            }
        }

        Board updated = boardRepository.save(board);
        return BoardDto.fromEntity(updated);
    }

    // ② softDelete
    public void softDeleteBoard(UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        board.setDeleted(true);
        board.setUpdatedAt(Timestamp.from(Instant.now()));
        boardRepository.save(board);
    }


}
