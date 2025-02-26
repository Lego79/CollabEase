package com.master.side.presentation.controller;

import com.master.side.application.dto.CombinedTaskBoardCommentResponse;
import com.master.side.application.dto.TaskResponseDto;
import com.master.side.application.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @GetMapping("/paged")
    public ResponseEntity<Page<CombinedTaskBoardCommentResponse>> getAllTasksPaged(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Page<CombinedTaskBoardCommentResponse> pagedData =
                taskService.getAllCombinedDataPaged(page, size);
        return ResponseEntity.ok(pagedData);
    }



    @GetMapping("/task-data")
    public ResponseEntity<List<TaskResponseDto>> getAllTaskData() {
        List<TaskResponseDto> tasks = taskService.getAllTaskData();
        return ResponseEntity.ok(tasks);
    }



}
