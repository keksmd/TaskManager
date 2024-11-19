package com.example.demo.model.dto;

import com.example.demo.model.Priority;
import com.example.demo.model.Status;

import java.util.Set;


public record TaskRequest(Priority priority, String header, Status status, CommentDtoRequest commentDtoRequest, Long assigneeId) {
}
