
package com.example.demo.controller;

import com.example.demo.exception.Forbidden;
import com.example.demo.model.dto.TaskRequest;
import com.example.demo.model.dto.TaskResponse;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskControllerImpl implements TaskController {
    private final TaskService taskService;

    @Override
    public List<TaskResponse> getAll(Pageable pageable, UserDetails details) {
        return taskService.getAllTasks(pageable);
    }

    @PreAuthorize("customUserDetailsService.isAdmin(details)")
    @Override
    public @ResponseBody TaskResponse createTask(UserDetails details, TaskRequest task) {
        return taskService.createTask(details,task);
    }

    @Override
    @PreAuthorize("customUserDetailsService.isAdminOrAssigneeOfTaskAnd(details,id)")
    public @ResponseBody TaskResponse updateTask(UserDetails details, TaskRequest task, Long id) {
       return  taskService.updateTask(details,task,id);
    }

    @Override
    @PreAuthorize("customUserDetailsService.isAdminOrAssigneeOfTaskAnd(details,id)")
    public @ResponseBody TaskResponse deleteTask(UserDetails details, Long id) throws ChangeSetPersister.NotFoundException, Forbidden {
        return taskService.deleteTask(details, id);
    }

    @Override
    public @ResponseBody TaskResponse getTaskById(UserDetails details, Long id) {
        return taskService.getTaskById(details, id);
    }
}
