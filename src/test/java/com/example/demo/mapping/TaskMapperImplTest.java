package com.example.demo.mapping;

import com.example.demo.model.Priority;
import com.example.demo.model.Status;
import com.example.demo.model.data.Comment;
import com.example.demo.model.data.Task;
import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.CommentDtoResponse;
import com.example.demo.model.dto.TaskResponse;
import com.example.demo.model.dto.UserDtoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension .class)
public class TaskMapperImplTest {

    @InjectMocks
    private TaskMapperImpl taskMapper;

    @Mock
    private UserMapper userMapper;
    @Mock
    private CommentMapper commentMapper;


    private Task taskEntity;
    private UserEntity author;
    private UserEntity assignee;

    @BeforeEach
    void setUp() {


        // Создание тестовых объектов
        author = new UserEntity();
        author.setId(1L);
        author.setUsername("authorUser");
        author.setPasswordConfirm("pass");
        author.setPassword("pass");
        author.setEmail("author@example.com");

        assignee = new UserEntity();
        assignee.setId(2L);
        assignee.setPasswordConfirm("pass");
        assignee.setPassword("pass");
        assignee.setUsername("assigneeUser");
        assignee.setEmail("assignee@example.com");

        taskEntity = new Task();
        taskEntity.setId(1L);
        taskEntity.setHeader("Test Task");
        taskEntity.setPriority(Priority.HIGH);
        taskEntity.setStatus(Status.OPEN);
        taskEntity.setAuthor(author);
        taskEntity.setAssignee(assignee);
        Comment c = new Comment();
        c.setBody("comment");
        taskEntity.setComments(new HashSet<>(List.of(c)));
    }

    @Test
    void testToResponse() {
        long i = 1L;
        when(commentMapper.toResponse(any(Comment.class))).thenReturn(new CommentDtoResponse(null,new HashSet<>(),++i,"comment",new UserDtoResponse(1L,"authorUser", "author@example.com")));
        // Подготовка
        when(userMapper.toDto(author)).thenReturn(new UserDtoResponse(1L,"authorUser","author@example.com"));
        when(userMapper.toDto(assignee)).thenReturn(new UserDtoResponse(2L,"assigneeUser","assignee@example.com"));

        // Выполнение
        TaskResponse response = taskMapper.toResponse(taskEntity);

        // Проверка
        assertEquals(taskEntity.getId(), response.id());
        assertEquals(taskEntity.getHeader(), response.header());
        assertEquals(taskEntity.getPriority(), response.priority());
        assertEquals(taskEntity.getStatus(), response.status());
        assertEquals("authorUser", response.author().username());
        assertEquals("assigneeUser", response.assignee().username());
        assertEquals("comment", response.comments().stream().toList().get(0).body());
        verify(userMapper, times(1)).toDto(author);
        verify(userMapper, times(1)).toDto(assignee);

    }
}
