package com.example.demo.model.dto;

import java.util.Set;

public record CommentDtoRequest(Long parentId,
                                Set<Long> childrenId,
                                String body) {
}
