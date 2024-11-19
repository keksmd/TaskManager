package com.example.demo.dto;

import java.util.Set;

public record CommentDtoRequest(Long parentId,
                                Set<Long> childrenId,
                                Long id,
                                String body) {
}
