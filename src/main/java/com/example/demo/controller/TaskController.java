package com.example.demo.controller;

import com.example.demo.model.dto.TaskRequest;
import com.example.demo.model.dto.TaskResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("taskmaster/tasks")
public interface TaskController {
    @GetMapping
    @ResponseBody
    Page<TaskResponse> getAll(Pageable pageable, @AuthenticationPrincipal UserDetails details);
    @PostMapping
    @ResponseBody TaskResponse createTask(@AuthenticationPrincipal UserDetails details, @RequestBody TaskRequest task);

    @PutMapping("/{id}")
    @ResponseBody TaskResponse updateTask(@AuthenticationPrincipal UserDetails details, @RequestBody TaskRequest task, @PathVariable Long id);

    @DeleteMapping("/{id}")
    @ResponseBody TaskResponse deleteTask(@AuthenticationPrincipal UserDetails details, @PathVariable Long id) throws ChangeSetPersister.NotFoundException;

    @GetMapping("/{id}")
    @ResponseBody TaskResponse getTaskById(@AuthenticationPrincipal UserDetails details, @PathVariable Long id);
}
