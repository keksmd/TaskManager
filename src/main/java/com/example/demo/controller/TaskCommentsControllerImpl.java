package com.example.demo.controller;

import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.CommentDtoRequest;
import com.example.demo.model.dto.CommentDtoResponse;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.TaskCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TaskCommentsControllerImpl implements TaskCommentsController {
    private final TaskCommentService taskCommentService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    @PreAuthorize("customUserDetailsService.isAdminOrAssigneeOfTaskAnd(userDetails,taskId)")
    public @ResponseBody CommentDtoResponse addComment(UserDetails userDetails, CommentDtoRequest commentDtoRequest, Long taskId) {
        return taskCommentService.addComment(((UserEntity) customUserDetailsService.loadUserByUsername(userDetails.getUsername())).getId(), commentDtoRequest, taskId);
    }

    @Override
    @PreAuthorize("customUserDetailsService.isAdminOrAssigneeOfTaskAnd(userDetails,taskId)")
    public @ResponseBody CommentDtoResponse removeComment(UserDetails userDetails, Long taskId, Long commentId) {
        return taskCommentService.removeComment(taskId, commentId);
    }

    @Override
    @PreAuthorize("customUserDetailsService.isAdminOrAssigneeOfTaskAnd(userDetails,taskId)")
    public @ResponseBody CommentDtoResponse updateComment(UserDetails userDetails, CommentDtoRequest commentDtoRequest, Long commentId, Long taskId) {
        return taskCommentService.updateComment(((UserEntity) customUserDetailsService.loadUserByUsername(userDetails.getUsername())).getId(), commentDtoRequest, commentId, taskId);
    }
}
