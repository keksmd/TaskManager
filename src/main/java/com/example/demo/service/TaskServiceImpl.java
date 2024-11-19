package com.example.demo.service;

import com.example.demo.mapping.TaskMapper;
import com.example.demo.model.data.Task;
import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.TaskRequest;
import com.example.demo.model.dto.TaskResponse;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final CustomUserDetailsService customUserDetailsService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskResponse> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).stream().map(taskMapper::toResponse).toList();
    }

    @Override
    public TaskResponse createTask(UserDetails userDetails, TaskRequest task) {
        Task taskEntity = new Task();
        UserEntity author = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        fillEntity(task, taskEntity, author);
        taskRepository.save(taskEntity);
        return taskMapper.toResponse(taskEntity);
    }

    @Override
    public TaskResponse updateTask(UserDetails userDetails, TaskRequest task, Long id) {
        Task taskEntity = taskRepository.getReferenceById(id);
        UserEntity author = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        fillEntity(task, taskEntity, author);
        taskRepository.save(taskEntity);
        return taskMapper.toResponse(taskEntity);

    }

    private void fillEntity(TaskRequest task, Task taskEntity, UserEntity author) {
        BeanUtils.copyProperties(task, taskEntity);
        taskEntity.setHeader(task.header());
        taskEntity.setPriority(task.priority());
        taskEntity.setStatus(task.status());
        if(task.assigneeId() != null){
        taskEntity.setAssignee(userRepository.getReferenceById(task.assigneeId()));}
        taskEntity.setAuthor(author);
    }

    @Override
    public TaskResponse deleteTask(UserDetails userDetails, Long id)  {
        Task task = taskRepository.getReferenceById(id);
        taskRepository.delete(task);
        return taskMapper.toResponse(task);
    }

    @Override
    public TaskResponse getTaskById(UserDetails userDetails, Long id) {
        return taskMapper.toResponse(taskRepository.getReferenceById(id));
    }
}
