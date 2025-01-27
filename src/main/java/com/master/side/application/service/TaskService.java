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

//    보드를  구성할 때, 어떻게 잘 짤까?, 인증 인가, 작성자, view 등등 고민한 흔적,
//    전후 과정, 고민들,
//    n + 1,
//    id - uuid로 관리
//    uuid로 관리 해서 식별 할 수 있는 키,
//    in-memory db - singlside, 서버든 spring security든
//    보드, 작성자 파일 IO
//    스프링을 잘 썼느냐 까지,
//
//    초보 vs 중급자 보드 차이
//    초보 - 요청 받음, 파일 저장 시 byte 배열로 clob blob , clob, blob 리턴 기본적인 구현
//    중급 - aop를 써서 특정 method, 특정 packge 밑에 aspect 사용, 메서드의 전 후 처리 로그 찍기
//
//    common - 작성자, 시간 등등
//    로그를 위해서, 사용해야 하는 다채로운 스프링 기술들
//    웹플럭스 - 리액티브 프로그래밍
//
//    exception 깔끔하게 사용하는 방법,
//    예외를 안하거나 exception e만 try catch 써서 사용하는 것은 지양
//
//    aop를 잘 사용하면 중복된 try catch를 제거할 수 있다
//
//    예상가능한 예외 - 이 클래스는 이 예외를 발생 시킨다 인지
//
//    httpStatus code 적절히 사용하기
//
//    자료형 - Json - message Converter 잭슨 라이브러리 - 프로퍼티
//
//    data parsing
//
//
//    domain -
//    redis, kafka, kubernetis,
//
//    스택 - ㅡㅡㅡ 보고 가는게 낫지 않나

    //할일 - 프론트엔드 토큰으로 헤더에 담아서 접근할 수 있께, 로그인 후에
    //
// src/types/TaskTypes.ts
//    export interface TaskCommentReply {
//        commentId: number;
//        commentContent: string;
//        commenterUsername: string;
//        commentCreatedAt: string;
//        replies: TaskCommentReply[];
//    }
//
//    export interface TaskComment {
//        commentId: number;
//        commentContent: string;
//        commenterUsername: string;
//        commentCreatedAt: string;
//        replies: TaskCommentReply[];
//    }
//
//    export interface TaskData {
//        taskId: number;
//        taskTitle: string;
//        taskDescription: string;
//        taskStartDate: string;
//        taskEndDate: string;
//        taskStatus: string;
//        taskCreatedAt: string;
//        taskUpdatedAt: string;
//        boardId: number;
//        boardTitle: string;
//        boardContent: string;
//        boardViewCount: number;
//        boardCreatedAt: string;
//        boardUpdatedAt: string;
//        comments: TaskComment[];
//        username: string;
//        nickname: string;
//    }

    // src/components/Board.tsx
//
//    // src/types/BoardTypes.ts
//    export interface CardItem {
//        id: string;
//        title: string;
//        content: string;
//    }
//
//    export interface ColumnData {
//        id: string;
//        title: string;
//        cards: CardItem[];
//    }
//import React, { useEffect, useState } from 'react';
//import { DragDropContext, DropResult } from '@hello-pangea/dnd';
//import styled from 'styled-components';
//import Column from './Column';
//import { TaskData } from '../types/TaskTypes';
//import { CardItem, ColumnData } from '../types/BoardTypes';
//
//const BoardContainer = styled.div`
//    display: flex;
//    align-items: flex-start;
//    padding: 20px;
//    background-color: #0079bf;
//    min-height: 100vh;
//    overflow-x: auto;
//`;
//
//const Board: React.FC = () => {
//  const [columns, setColumns] = useState<ColumnData[]>([]);
//
//        // 1) API 호출
//        useEffect(() => {
//    const fetchTasks = async () => {
//      const response = await fetch('http://localhost:8080/api/tasks');
//      const data: TaskData[] = await response.json();
//
//            // 2) 상태별로 카드 아이템 생성
//      const todoCards: CardItem[] = data
//                    .filter(task => task.taskStatus === 'PENDING')
//        .map(task => ({
//                    id: String(task.taskId),
//                    title: task.taskTitle,
//                    content: task.taskDescription
//        }));
//
//      const inProgressCards: CardItem[] = data
//                    .filter(task => task.taskStatus === 'IN_PROGRESS')
//        .map(task => ({
//                    id: String(task.taskId),
//                    title: task.taskTitle,
//                    content: task.taskDescription
//        }));
//
//      const doneCards: CardItem[] = data
//                    .filter(task => task.taskStatus === 'COMPLETED')
//        .map(task => ({
//                    id: String(task.taskId),
//                    title: task.taskTitle,
//                    content: task.taskDescription
//        }));
//
//            // 3) 컬럼 리스트 준비
//      const newColumns: ColumnData[] = [
//            { id: 'todo', title: '할 일', cards: todoCards },
//            { id: 'in-progress', title: '진행 중', cards: inProgressCards },
//            { id: 'done', title: '완료', cards: doneCards }
//      ];
//
//            setColumns(newColumns);
//        };
//
//        fetchTasks();
//  }, []);
//
//        // 4) 드래그 앤 드롭 처리
//  const onDragEnd = (result: DropResult) => {
//    const { destination, source } = result;
//            if (!destination) return;
//            if (
//                    destination.droppableId === source.droppableId &&
//                            destination.index === source.index
//            ) {
//                return;
//            }
//
//            // 현재 컬럼, 타겟 컬럼 찾기
//    const sourceColumn = columns.find(col => col.id === source.droppableId);
//    const destColumn = columns.find(col => col.id === destination.droppableId);
//            if (!sourceColumn || !destColumn) return;
//
//            // 원본/대상 카드 배열 복제
//    const newColumns = [...columns];
//    const sourceCards = [...sourceColumn.cards];
//    const destCards =
//                    sourceColumn.id === destColumn.id ? sourceCards : [...destColumn.cards];
//
//            // 카드 이동
//    const [movedCard] = sourceCards.splice(source.index, 1);
//            destCards.splice(destination.index, 0, movedCard);
//
//            // 컬럼에 새 카드 배열 갱신
//            newColumns.forEach(col => {
//            if (col.id === sourceColumn.id) {
//                col.cards = sourceCards;
//            }
//            if (col.id === destColumn.id) {
//                col.cards = destCards;
//            }
//    });
//
//            setColumns(newColumns);
//        };
//
//        return (
//                <DragDropContext onDragEnd={onDragEnd}>
//      <BoardContainer>
//                {columns.map(column => (
//                        <Column key={column.id} column={column} />
//        ))}
//      </BoardContainer>
//    </DragDropContext>
//  );
//    };
//
//    export default Board;


}