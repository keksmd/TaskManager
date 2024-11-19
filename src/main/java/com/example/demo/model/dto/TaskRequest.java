package com.example.demo.dto;

import com.example.demo.model.data.Status;

import java.util.Set;


public record TaskRequest(String header, Status status, Long id, Long authorId, Set<Long> commentsId, Long assigneeId) {
}
