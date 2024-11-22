
package com.example.demo.controller;

import com.example.demo.model.dto.TaskRequest;
import com.example.demo.model.dto.TaskResponse;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j // Аннотация для логирования с помощью Lombok
@RestController
@RequiredArgsConstructor
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;
   public final CustomUserDetailsService customUserDetailsService;


    @Override
    public Page<TaskResponse> getAll(Pageable pageable, UserDetails details) {
        log.info("Fetching all tasks for user: {}", details.getUsername());
        Page<TaskResponse> tasks = taskService.getAllTasks(pageable);
        log.info("Fetched {} tasks", tasks.getTotalElements());
        return tasks;
    }

    @PreAuthorize("customUserDetailsService.isAdmin(details)")
    @Override
    public @ResponseBody TaskResponse createTask(UserDetails details, TaskRequest task) {
        log.info("Creating task for user: {}", details.getUsername());
        TaskResponse createdTask = taskService.createTask(details, task);
        log.info("Task created: {}", createdTask);
        return createdTask;
    }

    @Override
    @PreAuthorize("customUserDetailsService.isAdminOrAssignee(details,id)")
    public @ResponseBody TaskResponse updateTask(UserDetails details, TaskRequest task, Long id) {
        log.info("Updating task with id: {} for user: {}", id, details.getUsername());
        TaskResponse updatedTask = taskService.updateTask(details, task, id);
        log.info("Task updated: {}", updatedTask);
        return updatedTask;
    }

    @Override
    @PreAuthorize("customUserDetailsService.isAdminOrAssignee(details,id)")
    public @ResponseBody TaskResponse deleteTask(UserDetails details, Long id) {
        log.info("Deleting task with id: {} for user: {}", id, details.getUsername());
        TaskResponse deletedTask = taskService.deleteTask(details, id);
        log.info("Task deleted: {}", deletedTask);
        return deletedTask;
    }

    @Override
    public @ResponseBody TaskResponse getTaskById(UserDetails details, Long id) {
        log.info("Fetching task by id: {} for user: {}", id, details.getUsername());
        TaskResponse task = taskService.getTaskById(details, id);
        log.info("Fetched task: {}", task);
        return task;
    }
}
