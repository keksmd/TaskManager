package com.example.demo.controller;

import com.example.demo.model.dto.CommentDtoRequest;
import com.example.demo.model.dto.CommentDtoResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/taskmaster/tasks/{taskId}/comments")
public interface TaskCommentsController {
    @PostMapping()
    @ResponseBody
    CommentDtoResponse addComment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CommentDtoRequest commentDtoRequest, @PathVariable Long taskId);
    @DeleteMapping("/{commentId}")
    @ResponseBody
    CommentDtoResponse removeComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long taskId, @PathVariable Long commentId);
    @PutMapping("/{commentId}")
    @ResponseBody
    CommentDtoResponse updateComment(@AuthenticationPrincipal UserDetails userDetails,@RequestBody CommentDtoRequest commentDtoRequest,  @PathVariable Long commentId,@PathVariable Long taskId);
}
