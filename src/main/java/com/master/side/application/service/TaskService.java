package com.master.side.application.service;

import com.master.side.application.dto.CombinedTaskBoardCommentResponse;
import com.master.side.application.dto.TaskResponseDto;
import com.master.side.application.mapper.CombinedTaskBoardCommentMapper;
import com.master.side.domain.repository.BoardRepository;
import com.master.side.domain.repository.FilesRepository;
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
    private final FilesRepository fileRepository; // 주입

    public TaskService(TaskRepository taskRepository, BoardRepository boardRepository, FilesRepository fileRepository) {
        this.taskRepository = taskRepository;
        this.boardRepository = boardRepository;
        this.fileRepository = fileRepository;
    }

    public List<TaskResponseDto> getAllTaskData() {
        var tasks = taskRepository.findAll();
        return tasks.stream()
                .map(CombinedTaskBoardCommentMapper::toTaskResponseDto)
                .toList();
    }

    public List<CombinedTaskBoardCommentResponse> getAllCombinedData() {
        var boards = boardRepository.findAll();
        return boards.stream()
                .map(CombinedTaskBoardCommentMapper::toCombinedDto)
                .toList();
    }

    public List<CombinedTaskBoardCommentResponse> getCombinedDataByTaskId(UUID taskId) {
        var boards = boardRepository.findByTaskId(taskId);
        if (boards.isEmpty()) {
            throw new IllegalArgumentException("해당 Task에 해당하는 Board를 찾을 수 없습니다. Task ID: " + taskId);
        }
        return boards.stream()
                .map(CombinedTaskBoardCommentMapper::toCombinedDto)
                .toList();
    }
}
