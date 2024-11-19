package com.example.demo.dto;

import java.util.Set;

public record CommentDtoResponse(CommentDtoResponse parent,
                                 Set<CommentDtoResponse> children,
                                 Long id,
                                 String body) {
}
