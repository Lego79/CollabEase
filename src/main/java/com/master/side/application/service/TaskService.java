package com.master.side.application.service;

import com.master.side.application.dto.CombinedTaskBoardCommentDto;
import com.master.side.application.dto.TaskResponseDto;
import com.master.side.domain.model.Board;
import com.master.side.domain.model.Comment;
import com.master.side.domain.model.Member;
import com.master.side.domain.model.Task;
import com.master.side.domain.repository.TaskRepository;
import com.master.side.domain.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;

    public TaskService(TaskRepository taskRepository, BoardRepository boardRepository) {
        this.taskRepository = taskRepository;
        this.boardRepository = boardRepository;
    }

    /**
     * Task 목록을 모두 조회하여 TaskResponseDto로 변환
     */
    public List<TaskResponseDto> getAllTaskData() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::convertTaskToDto)
                .toList();
    }

    private TaskResponseDto convertTaskToDto(Task task) {
        return TaskResponseDto.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .startDate(task.getStartDate().toLocalDateTime())
                .endDate(task.getEndDate() != null ? task.getEndDate().toLocalDateTime() : null)
                .status(task.getStatus())
                .createdAt(task.getCreatedAt().toLocalDateTime())
                .updatedAt(task.getUpdatedAt().toLocalDateTime())
                .build();
    }

    /**
     * 전체 Board(게시글) 데이터를 Combined DTO로 반환
     * 각 Board는 독립적인 게시글로 출력됨 (Task 정보 및 댓글 포함)
     */
    public List<CombinedTaskBoardCommentDto> getAllCombinedData() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(this::convertBoardToCombinedDto)
                .toList();
    }

    /**
     * 특정 Task ID에 속하는 모든 Board(게시글) 데이터를 Combined DTO로 반환
     * (같은 Task에 속하더라도 각 Board는 독립적인 게시글로 처리)
     */
    public List<CombinedTaskBoardCommentDto> getCombinedDataByTaskId(UUID taskId) {
        List<Board> boards = boardRepository.findByTaskId(taskId);
        if (boards.isEmpty()) {
            throw new IllegalArgumentException("해당 Task에 해당하는 Board를 찾을 수 없습니다. Task ID: " + taskId);
        }
        return boards.stream()
                .map(this::convertBoardToCombinedDto)
                .toList();
    }

    /**
     * Board 엔티티를 CombinedTaskBoardCommentDto로 변환하는 메서드
     * -> Board에 연결된 Task와 작성자(Member), 댓글(Comment) 정보를 포함함.
     */
    private CombinedTaskBoardCommentDto convertBoardToCombinedDto(Board board) {
        // Board와 연관된 Task를 가져옴 (Board 엔티티에 getTask()가 정의되어 있다고 가정)
        Task task = board.getTask();
        Member member = task.getMember();
        List<CombinedTaskBoardCommentDto.CommentDto> commentDtos = board.getComments().stream()
                .map(this::convertCommentToDto)
                .toList();

        return CombinedTaskBoardCommentDto.builder()
                // === Task 정보 ===
                .taskId(task.getId())
                .taskTitle(task.getTitle())
                .taskDescription(task.getDescription())
                .taskStartDate(task.getStartDate().toLocalDateTime())
                .taskEndDate(task.getEndDate() != null ? task.getEndDate().toLocalDateTime() : null)
                .taskStatus(task.getStatus())
                .taskCreatedAt(task.getCreatedAt().toLocalDateTime())
                .taskUpdatedAt(task.getUpdatedAt().toLocalDateTime())
                // === Board 정보 ===
                .boardId(board.getId())
                .boardTitle(board.getTitle())
                .boardContent(board.getContent())
                .boardViewCount(board.getViewCount())
                .boardCreatedAt(board.getCreatedAt().toLocalDateTime())
                .boardUpdatedAt(board.getUpdatedAt().toLocalDateTime())
                // === Comment 정보 ===
                .comments(commentDtos)
                // === 작성자 정보 ===
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
    }

    /**
     * Comment 엔티티를 CommentDto로 변환하는 메서드
     * 대댓글이 있을 경우 재귀적으로 처리.
     */
    private CombinedTaskBoardCommentDto.CommentDto convertCommentToDto(Comment comment) {
        return CombinedTaskBoardCommentDto.CommentDto.builder()
                .commentId(comment.getId())
                .commentContent(comment.getContent())
                .commenterUsername(comment.getMember().getUsername())
                .commentCreatedAt(comment.getCreatedAt().toLocalDateTime())
                .replies(comment.getReplies().stream()
                        .map(this::convertCommentToDto)
                        .toList())
                .build();
    }
}
