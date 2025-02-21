package com.master.side.presentation.controller;

import com.master.side.application.dto.CombinedTaskBoardCommentResponse;
import com.master.side.application.dto.TaskResponseDto;
import com.master.side.application.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 전체 Task 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<CombinedTaskBoardCommentResponse>> getAllTasks() {
        List<CombinedTaskBoardCommentResponse> tasks = taskService.getAllCombinedData();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/task-data")
    public ResponseEntity<List<TaskResponseDto>> getAllTaskData() {
        List<TaskResponseDto> tasks = taskService.getAllTaskData();
        return ResponseEntity.ok(tasks);
    }


    /**
     * 단일 Task 조회
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<List<CombinedTaskBoardCommentResponse>> getTaskById(@PathVariable("taskId") UUID taskId) {
        List<CombinedTaskBoardCommentResponse> task = taskService.getCombinedDataByTaskId(taskId);
        return ResponseEntity.ok(task);
    }

}
