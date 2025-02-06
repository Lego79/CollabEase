package com.master.side.application.mapper;

import com.master.side.application.dto.CommentResponseDto;
import com.master.side.domain.model.Comment;

public class CommentMapper {

    public static CommentResponseDto toResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt().toInstant(),
                comment.getMember().getUsername()
        );
    }
}
