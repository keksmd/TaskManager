package com.example.demo.mapping;

import com.example.demo.model.data.Task;
import com.example.demo.model.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskMapperImpl implements TaskMapper {
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    @Override
    public TaskResponse toResponse(Task taskEntity) {
        return new TaskResponse(taskEntity.getPriority(),
                taskEntity.getHeader(),
                taskEntity.getStatus(),
                taskEntity.getId(),
                userMapper.toDto(taskEntity.getAuthor()),
                taskEntity.getComments().stream().map(commentMapper::toResponse).collect(Collectors.toSet()),
                userMapper.toDto(taskEntity.getAssignee()));
    }

    @Override
    public Task toEntity(TaskResponse taskResponse) {
        return null;
    }
}
