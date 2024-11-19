package com.example.demo.service;

import com.example.demo.model.dto.CommentDtoRequest;
import com.example.demo.model.dto.CommentDtoResponse;

public interface TaskCommentService {


    CommentDtoResponse addComment(Long issuerId, CommentDtoRequest commentDtoRequest, Long taskId);

    CommentDtoResponse removeComment(Long taskId, Long commentId);

    CommentDtoResponse updateComment(Long issuerId, CommentDtoRequest commentDtoRequest, Long commentId, Long taskId);
}
