package com.example.demo.model.dto;

import com.example.demo.model.Priority;
import com.example.demo.model.Status;


public record TaskRequest(Priority priority, String header, Status status, Long assigneeId) {
}
