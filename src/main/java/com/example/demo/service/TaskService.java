package com.example.demo.service;

import com.example.demo.model.dto.TaskRequest;
import com.example.demo.model.dto.TaskResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<TaskResponse> getAllTasks(Pageable pageable);
    TaskResponse createTask(@AuthenticationPrincipal UserDetails userDetails, TaskRequest task);

    TaskResponse updateTask(UserDetails userDetais, TaskRequest task, Long id);

    Optional<TaskResponse> deleteTask(UserDetails userDetails, Long id) ;

    TaskResponse getTaskById(UserDetails userDetails, Long id);
}
