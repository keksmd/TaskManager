package com.example.demo;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface TaskService {
    TaskResponse createTask(@AuthenticationPrincipal OAuth2ResourceServerProperties.Jwt jwt, TaskRequest task);
    TaskResponse updateTask(@AuthenticationPrincipal OAuth2ResourceServerProperties.Jwt jwt, TaskRequest task,Long id);
    TaskResponse deleteTask(@AuthenticationPrincipal OAuth2ResourceServerProperties.Jwt jwt,Long id);
}
