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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;

    public BoardService(BoardRepository boardRepository, TaskRepository taskRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.taskRepository = taskRepository;
        this.memberRepository = memberRepository;
    }

    // createBoard 메서드 수정: 요청에서 Task ID를 받아 Task 엔티티와 매핑하여 Board 생성
    public BoardDto createBoard(CreateBoardRequest request) {

        // 보드 작성, 주인이 작성,
        //스티키 세션, 세션에 활성화 되어있는 jwt token 식별하는 코드,
        //컨트롤러에서 validation을 통과하면, 이 코드가 실행됨 @Valid
        //보드 오기 전에, 현재 로그인한 사용자 식별자 확인후 로직 실행,
        //앞으로도 반복 될 코드 //        Member member = memberRepository.findById(currentUserId)
        // 반복을 줄일 수 있는 방법 찾아볼 것- aop로 빼는게 맞음

        UUID currentUserId = SecurityContextHelper.getCurrentUserId();

        // DB에서 Member 엔티티 조회
        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 요청에 포함된 Task ID로 Task 엔티티 조회
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        // Board 생성 시, task 필드를 함께 할당 (즉, 이 Board가 해당 Task에 종속됨)
        Board board = Board.builder()
                .title(request.getTitle())
                .member(member)
                .content(request.getContent())
                .task(task)  // Task와 연결
                .deleted(false)
                .build();

        boardRepository.save(board);

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
        board.setUpdatedAt(java.sql.Timestamp.from(java.time.Instant.now()));

        Board updated = boardRepository.save(board);
        return mapToDto(updated);
    }

    public void softDeleteBoard(UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        board.setDeleted(true);
        board.setUpdatedAt(java.sql.Timestamp.from(java.time.Instant.now()));
        boardRepository.save(board);
    }

    public List<BoardDto> getAllBoards() {
        // isDeleted = false 인 것만 조회
        List<Board> boards = boardRepository.findAllByDeletedFalse();
        return boards.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
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
