package com.master.side.application.service;

import com.master.side.application.dto.CombinedTaskBoardCommentResponse;
import com.master.side.application.dto.TaskResponseDto;
import com.master.side.application.mapper.CombinedTaskBoardCommentMapper;
import com.master.side.domain.model.Board;
import com.master.side.domain.repository.BoardRepository;
import com.master.side.domain.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
        var tasks = taskRepository.findAll();
        return tasks.stream()
                .map(CombinedTaskBoardCommentMapper::toTaskResponseDto)
                .toList();
    }

    public Page<CombinedTaskBoardCommentResponse> getAllCombinedDataPaged(int page, int size) {
        // 1) Pageable 생성 (page: 0-based 인덱스, size: 한 페이지당 개수)
        //    예) createdAt desc 로 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 2) BoardRepository에서 isDeleted=false 인 것만 페이징 조회
        Page<Board> boardPage = boardRepository.findAllByDeletedFalseOrderByCreatedAtAsc(pageable);

        // 3) 각각의 Board를 CombinedTaskBoardCommentResponse로 변환
        //    스프링 Data JPA의 map() 사용하면 편리
        Page<CombinedTaskBoardCommentResponse> resultPage = boardPage.map(CombinedTaskBoardCommentMapper::toCombinedDto);

        return resultPage;
    }


}
