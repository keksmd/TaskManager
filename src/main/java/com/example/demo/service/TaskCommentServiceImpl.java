package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapping.CommentMapper;
import com.example.demo.model.data.Comment;
import com.example.demo.model.data.Task;
import com.example.demo.model.dto.CommentDtoRequest;
import com.example.demo.model.dto.CommentDtoResponse;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j // Аннотация для логирования
public class TaskCommentServiceImpl implements TaskCommentService {
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    public final CommentDtoResponse addComment(Long issuerId, CommentDtoRequest commentDtoRequest, Long taskId) {
        log.info("Adding comment to task with ID: {}", taskId);
        Task task = taskRepository.findById(taskId).orElseThrow(()->new ResourceNotFoundException(String.format("Task with id %d not found",taskId)));
        var comments = task.getComments();
        Comment newComment = commentMapper.toEntity(commentDtoRequest, issuerId);
        commentRepository.save(newComment);
        comments.add(newComment);
        task.setComments(comments);
        taskRepository.save(task);
        log.info("Comment added with ID: {} to task ID: {}", newComment.getId(), taskId);
        return commentMapper.toResponse(newComment);
    }

    @Override
    public CommentDtoResponse removeComment(Long taskId, Long commentId) {
        log.info("Removing comment with ID: {} from task ID: {}", commentId, taskId);
        Task task = taskRepository.findById(taskId).orElseThrow(()->new ResourceNotFoundException(String.format("Task with id %d not found",taskId)));
        var comments = task.getComments();
        Comment commentToRemove = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException(String.format("Comment with id %d not found",commentId)));;
        comments.remove(commentToRemove);
        task.setComments(comments);
        taskRepository.save(task);
        log.info("Comment with ID: {} removed from task ID: {}", commentId, taskId);
        return commentMapper.toResponse(commentToRemove);
    }

    @Override
    public CommentDtoResponse updateComment(Long issuerId, CommentDtoRequest commentDtoRequest, Long commentId, Long taskId) {
        log.info("Updating comment with ID: {} on task ID: {}", commentId, taskId);
        Task task = taskRepository.findById(taskId).orElseThrow(()->new ResourceNotFoundException(String.format("Task with id %d not found",taskId)));
        var comments = task.getComments();
        Comment commentToUpdate = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException(String.format("Comment with id %d not found",commentId)));;
        if (comments.contains(commentToUpdate)) {
            Long id = commentToUpdate.getId();
            BeanUtils.copyProperties(commentMapper.toEntity(commentDtoRequest, issuerId), commentToUpdate, "id");
            commentToUpdate.setId(id);
            commentRepository.save(commentToUpdate);
            log.info("Comment with ID: {} updated on task ID: {}", commentId, taskId);
            return commentMapper.toResponse(commentToUpdate);
        } else {
            log.warn("Attempt to update nonexistent comment with ID: {} on task ID: {}", commentId, taskId);
            throw new RuntimeException("Comment does not exist on chosen Task");
        }
    }
}
