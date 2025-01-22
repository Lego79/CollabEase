package com.master.side.application.service;


import com.master.side.application.dto.TaskResponseDto;
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
     * 전체 Task 목록을 조회하여 DTO로 변환 후 반환
     */
    public List<TaskResponseDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 단일 Task 조회
     */
    public TaskResponseDto getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Task를 찾을 수 없습니다. ID: " + taskId));
        return convertToDto(task);
    }

    /**
     * Task -> TaskResponseDto 변환 메서드
     */
    private TaskResponseDto convertToDto(Task task) {
        Member member = task.getMember(); // @ManyToOne 관계로 설정되어 있다고 가정
        return new TaskResponseDto(
                task.getId(),
                member.getMemberId(),
                member.getUsername(),
                member.getNickname(),
                task.getTitle(),
                task.getDescription(),
                task.getStartDate().toLocalDateTime(),
                task.getEndDate() != null ? task.getEndDate().toLocalDateTime() : null,
                task.getStatus(),
                task.getCreatedAt().toLocalDateTime(),
                task.getUpdatedAt().toLocalDateTime()
        );
    }
}