package com.master.side.application.service;


import com.master.side.application.dto.CombinedTaskBoardCommentDto;
import com.master.side.domain.model.Board;
import com.master.side.domain.model.Comment;
import com.master.side.domain.model.Member;
import com.master.side.domain.model.Task;
import com.master.side.domain.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * 전체 Task 목록을 조회하여 Combined DTO로 변환 후 반환
     */
    public List<CombinedTaskBoardCommentDto> getAllCombinedData() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::convertToCombinedDto)
                .toList();
    }

    /**
     * 단일 Task 조회 후 Combined DTO로 변환
     */
    public CombinedTaskBoardCommentDto getCombinedDataByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Task를 찾을 수 없습니다. ID: " + taskId));
        return convertToCombinedDto(task);
    }

    /**
     * Task -> CombinedTaskBoardCommentDto 변환 메서드
     */
    private CombinedTaskBoardCommentDto convertToCombinedDto(Task task) {
        Member member = task.getMember();
        Board board = task.getBoard();
        List<CombinedTaskBoardCommentDto.CommentDto> commentDtos = board.getComments().stream()
                .map(this::convertCommentToDto)
                .collect(Collectors.toList());

        return CombinedTaskBoardCommentDto.builder()
                // Task 정보
                .taskId(task.getId())
                .taskTitle(task.getTitle())
                .taskDescription(task.getDescription())
                .taskStartDate(task.getStartDate().toLocalDateTime())
                .taskEndDate(task.getEndDate() != null ? task.getEndDate().toLocalDateTime() : null)
                .taskStatus(task.getStatus())
                .taskCreatedAt(task.getCreatedAt().toLocalDateTime())
                .taskUpdatedAt(task.getUpdatedAt().toLocalDateTime())

                // Board 정보
                .boardId(board.getId())
                .boardTitle(board.getTitle())
                .boardContent(board.getContent())
                .boardViewCount(board.getViewCount())
                .boardCreatedAt(board.getCreatedAt().toLocalDateTime())
                .boardUpdatedAt(board.getUpdatedAt().toLocalDateTime())

                // Comment 정보
                .comments(commentDtos)

                // User 정보
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
    }

    /**
     * Comment -> CommentDto 변환 메서드
     */
    private CombinedTaskBoardCommentDto.CommentDto convertCommentToDto(Comment comment) {
        return CombinedTaskBoardCommentDto.CommentDto.builder()
                .commentId(comment.getId())
                .commentContent(comment.getContent())
                .commenterUsername(comment.getMember().getUsername())
                .commentCreatedAt(comment.getCreatedAt().toLocalDateTime())
                .replies(comment.getReplies().stream()
                        .map(this::convertCommentToDto)
                        .collect(Collectors.toList()))
                .build();
    }


}

