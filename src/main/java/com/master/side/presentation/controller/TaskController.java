package com.master.side.presentation.controller;

import com.master.side.application.dto.CombinedTaskBoardCommentDto;
import com.master.side.application.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 전체 Task 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<CombinedTaskBoardCommentDto>> getAllTasks() {
        List<CombinedTaskBoardCommentDto> tasks = taskService.getAllCombinedData();
        return ResponseEntity.ok(tasks);
    }

    /**
     * 단일 Task 조회
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<CombinedTaskBoardCommentDto> getTaskById(@PathVariable("taskId") Long taskId) {
        CombinedTaskBoardCommentDto task = taskService.getCombinedDataByTaskId(taskId);
        return ResponseEntity.ok(task);
    }

}
