package com.example.demo;

import com.example.demo.mapping.CommentMapperImpl;
import com.example.demo.mapping.UserMapper;
import com.example.demo.model.data.Comment;
import com.example.demo.model.dto.CommentDtoRequest;
import com.example.demo.model.dto.CommentDtoResponse;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class CommentMapperImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CommentMapperImpl commentMapper;

    private Comment comment;
    private CommentDtoRequest commentDtoRequest;
    private CommentDtoResponse commentDtoResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создание тестового комментария
        comment = new Comment();
        comment.setId(1L);
        comment.setBody("This is a comment");

        // Создание тестового запроса
        commentDtoRequest = new CommentDtoRequest(4L,new HashSet<>(List.of(2L, 1L)),"This is a comment");

        
        // Создание тестового ответа
        commentDtoResponse = new CommentDtoResponse(
                null, // Параметр parent
                null, // Параметр children
                1L,
                "This is a comment",
                null // Параметр authorDto
        );
    }

    @Test
    public void testToResponse() {
        // Подготовка данных
        Comment parentComment = new Comment();
        parentComment.setId(4L);
        when(comment.getParent()).thenReturn(parentComment);
        when(comment.getId()).thenReturn(1L);
        when(comment.getBody()).thenReturn("This is a comment");
        when(userMapper.toDto(any())).thenReturn(null); // Можно заменить на конкретный тестовый объект

        // Вызов метода
        CommentDtoResponse response = commentMapper.toResponse(comment);

        // Проверка результатов
        assertNotNull(response);
        assertEquals("This is a comment", response.body());
        assertEquals(1L, response.id());
    }

    @Test
    public void testToEntity() {
        // Подготовка данных
        when(commentRepository.findAllById(commentDtoRequest.childrenId())).thenReturn(List.of());
        when(commentRepository.findById(commentDtoRequest.parentId())).thenReturn(Optional.of(comment));

        // Вызов метода
        Comment entity = commentMapper.toEntity(commentDtoRequest,1L);

        // Проверка результатов
        assertNotNull(entity);
        assertEquals("This is a comment", entity.getBody());
        assertEquals(comment.getAuthor(), entity.getAuthor());
        assertNotNull(entity.getChildren());
        assertNotNull(entity.getParent());
    }

    @Test

    public void testToEntityWithNullReferences() {
        // Изменение запроса так, чтобы parent и author отсутствовали
        commentDtoRequest = new CommentDtoRequest(null,new HashSet<>(List.of(2L, 1L)),"This is a comment");


        // Вызов метода
        Comment entity = commentMapper.toEntity(commentDtoRequest,null);

        // Проверка результатов
        assertNotNull(entity);
        assertEquals("This is a comment", entity.getBody());
        assertNull(entity.getParent());
        assertNull(entity.getAuthor());
    }
}

