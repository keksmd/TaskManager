package com.example.demo.model.dto;

import com.example.demo.model.Priority;
import com.example.demo.model.Status;

import java.util.Set;


public record TaskResponse(Priority priority,String header, Status status, Long id, UserDto author, Set<CommentDtoResponse> comments, UserDto assignee) {
}
