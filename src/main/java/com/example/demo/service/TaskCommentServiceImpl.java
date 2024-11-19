package com.example.demo.service;

import com.example.demo.mapping.CommentMapper;
import com.example.demo.model.data.Comment;
import com.example.demo.model.data.Task;
import com.example.demo.model.dto.CommentDtoRequest;
import com.example.demo.model.dto.CommentDtoResponse;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class TaskCommentServiceImpl implements TaskCommentService {
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    @Override
    public final CommentDtoResponse addComment(Long issuerid, CommentDtoRequest commentDtoRequest, Long taskId) {
        Task task = taskRepository.getReferenceById(taskId);
        var comments = task.getComments();
        var newComment = commentRepository.save(commentMapper.toEntity(commentDtoRequest,issuerid));
        comments.add(newComment);
        task.setComments(comments);
        taskRepository.save(task);
        return commentMapper.toResponse(newComment);

    }

    @Override
    public CommentDtoResponse removeComment(Long taskId, Long commentId) {
        Task task = taskRepository.getReferenceById(taskId);
        var comments = task.getComments();
        Comment commentToRemove = commentRepository.getReferenceById(commentId);
        comments.remove(commentToRemove);
        task.setComments(comments);
        taskRepository.save(task);
        return commentMapper.toResponse(commentToRemove);
    }

    @Override
    public CommentDtoResponse updateComment(Long issuerId, CommentDtoRequest commentDtoRequest, Long commentId, Long taskId) {
        Task task = taskRepository.getReferenceById(taskId);
        var comments = task.getComments();
        Comment commentToUpdate = commentRepository.getReferenceById(commentId);
        if(comments.contains(commentToUpdate)){
            Long id = commentToUpdate.getId();
            BeanUtils.copyProperties(commentMapper.toEntity(commentDtoRequest,issuerId),commentToUpdate);
            commentToUpdate.setId(id);
            commentRepository.save(commentToUpdate);
            return commentMapper.toResponse(commentToUpdate);
        }else {
            throw new RuntimeException("Comment does not exist on chosen Task");
        }
    }
}
