package com.example.demo.service;

import com.example.demo.model.dto.TaskRequest;
import com.example.demo.model.dto.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface TaskService {
    Page<TaskResponse> getAllTasks(Pageable pageable);
    TaskResponse createTask(UserDetails userDetails, TaskRequest task);

    TaskResponse updateTask(UserDetails userDetais, TaskRequest task, Long id);

    TaskResponse deleteTask(UserDetails userDetails, Long id) ;

    TaskResponse getTaskById(UserDetails userDetails, Long id);
}
