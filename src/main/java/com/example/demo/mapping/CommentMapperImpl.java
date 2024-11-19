package com.example.demo.mapping;

import com.example.demo.model.data.Comment;
import com.example.demo.model.dto.CommentDtoRequest;
import com.example.demo.model.dto.CommentDtoResponse;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Component
public class CommentMapoperImpl implements CommentMapper{
    private final CommentRepository commentRepository;
    @Override
    public CommentDtoResponse toResponse(Comment comment) {
        return new CommentDtoResponse(comment.getParent()==null?null:toResponse(comment.getParent()),comment.getChildren()==null?null:new HashSet<>(comment.getChildren().stream().map(this::toResponse).collect(Collectors.toSet())), comment.getId(), comment.getBody() );
    }

    @Override
    public Comment toEntity(CommentDtoRequest commentDtoRequest) {
      Comment comment = new Comment();
      comment.setBody(commentDtoRequest.body());
      comment.setChildren(new HashSet<>(commentRepository.findAllById(commentDtoRequest.childrenId())));
      comment.setParent(commentRepository.findById(commentDtoRequest.parentId()).orElse(null));
      return  comment;
    }


}
