package com.example.demo.mapping;

import com.example.demo.model.data.Task;
import com.example.demo.model.dto.TaskResponse;

public interface TaskMapper {
    TaskResponse toResponse(Task taskEntity);
    Task toEntity(TaskResponse taskResponse);
}
