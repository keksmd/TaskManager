package com.example.demo.model.dto;

import java.util.Set;

public record CommentDtoResponse(CommentDtoResponse parent,
                                 Set<CommentDtoResponse> children,
                                 Long id,
                                 String body,UserDto author) {
}
