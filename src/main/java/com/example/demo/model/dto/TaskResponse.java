package com.example.demo.dto;

import com.example.demo.model.data.Status;

import java.util.Set;


public record TaskResponse(String header, Status status, Long id, UserReposnse author, Set<CommentDtoResponse> comments, UserReposnse assignee) {
}
