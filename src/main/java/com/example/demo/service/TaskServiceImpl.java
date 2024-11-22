package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapping.TaskMapper;
import com.example.demo.model.data.Task;
import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.TaskRequest;
import com.example.demo.model.dto.TaskResponse;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j // Аннотация для логирования с помощью Lombok
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    public Page<TaskResponse> getAllTasks(Pageable pageable) {
        log.info("Fetching all tasks with pagination: {}", pageable);
        Page<TaskResponse> tasks = taskRepository.findAll(pageable).map(taskMapper::toResponse);
        log.info("Fetched {} tasks", tasks.getTotalElements());
        return tasks;
    }

    @Override
    public TaskResponse createTask(UserDetails userDetails, TaskRequest task) {
        log.info("Creating task for user: {}", userDetails.getUsername());
        Task taskEntity = new Task();
        UserEntity author = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + userDetails.getUsername()));

        fillEntity(task, taskEntity, author);
        taskRepository.save(taskEntity);

        log.info("Task created with ID: {}", taskEntity.getId());
        return taskMapper.toResponse(taskEntity);
    }

    @Override
    public TaskResponse updateTask(UserDetails userDetails, TaskRequest task, Long id) {
        log.info("Updating task with ID: {} for user: {}", id, userDetails.getUsername());
        Task taskEntity = taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format("Task with id %d not found",id)));;
        fillEntity(task, taskEntity, taskEntity.getAuthor());
        taskRepository.save(taskEntity);

        log.info("Task updated with ID: {}", taskEntity.getId());
        return taskMapper.toResponse(taskEntity);
    }

    private void fillEntity(TaskRequest task, Task taskEntity, UserEntity author) {
        BeanUtils.copyProperties(task, taskEntity);
        taskEntity.setHeader(task.header());
        taskEntity.setPriority(task.priority());
        taskEntity.setStatus(task.status());
        if (task.assigneeId() != null) {
            taskEntity.setAssignee(userRepository.findById(task.assigneeId()).orElseThrow(()->new ResourceNotFoundException(String.format("User with id %d not found",task.assigneeId()))));
        }

        taskEntity.setAuthor(author);
    }

    @Override
    public TaskResponse deleteTask(UserDetails userDetails, Long id) {
        log.info("Deleting task with ID: {} for user: {}", id, userDetails.getUsername());
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %d not found", id)));
        taskRepository.delete(task);
        log.info("Task deleted with ID: {}", id);
        return taskMapper.toResponse(task);
    }

    @Override
    public TaskResponse getTaskById(UserDetails userDetails, Long id) {
        log.info("Fetching task by ID: {} for user: {}", id, userDetails.getUsername());
        TaskResponse taskResponse = taskMapper.toResponse(taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format("Task with id %d not found",id))));
        log.info("Fetched task with ID: {}", id);
        return taskResponse;
    }
}
