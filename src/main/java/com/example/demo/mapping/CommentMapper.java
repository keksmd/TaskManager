package com.example.demo.mapping;

import com.example.demo.model.data.Comment;
import com.example.demo.model.dto.CommentDtoRequest;
import com.example.demo.model.dto.CommentDtoResponse;

public interface CommentMapper {
    CommentDtoResponse toResponse(Comment comment);

    Comment toEntity(CommentDtoRequest commentDtoRequest, Long authorId);
}
