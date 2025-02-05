package com.master.side.application.service;

import com.master.side.application.dto.CombinedTaskBoardCommentDto;
import com.master.side.application.dto.TaskResponseDto;
import com.master.side.application.mapper.CombinedTaskBoardCommentMapper;
import com.master.side.domain.model.Board;
import com.master.side.domain.model.Task;
import com.master.side.domain.repository.BoardRepository;
import com.master.side.domain.repository.TaskRepository;
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

    public List<TaskResponseDto> getAllTaskData() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(CombinedTaskBoardCommentMapper::toTaskResponseDto)
                .toList();
    }

    public List<CombinedTaskBoardCommentDto> getAllCombinedData() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(CombinedTaskBoardCommentMapper::toCombinedDto)
                .toList();
    }

    public List<CombinedTaskBoardCommentDto> getCombinedDataByTaskId(UUID taskId) {
        List<Board> boards = boardRepository.findByTaskId(taskId);
        if (boards.isEmpty()) {
            throw new IllegalArgumentException("해당 Task에 해당하는 Board를 찾을 수 없습니다. Task ID: " + taskId);
        }
        return boards.stream()
                .map(CombinedTaskBoardCommentMapper::toCombinedDto)
                .toList();
    }
}