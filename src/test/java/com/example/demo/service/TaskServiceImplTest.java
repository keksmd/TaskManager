package com.example.demo.service;

import com.example.demo.mapping.TaskMapper;
import com.example.demo.model.Priority;
import com.example.demo.model.Status;
import com.example.demo.model.data.Task;
import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.TaskRequest;
import com.example.demo.model.dto.TaskResponse;
import com.example.demo.model.dto.UserDtoResponse;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UserDetails userDetails;
    private TaskRequest taskRequest;
    private Task taskEntity;
    private UserEntity author;
    private UserEntity assignee;
    private TaskResponse taskResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создание тестовых данных
        userDetails = mock(UserDetails.class);
        taskRequest = new TaskRequest(Priority.HIGH, "testTask", Status.IN_REVIEW,  1L);
        taskEntity = new Task();
        assignee = new UserEntity();
        author = new UserEntity();
        author.setId(1L);
        taskResponse = new TaskResponse(Priority.HIGH, "testTask", Status.IN_REVIEW, 1L, new UserDtoResponse(1L, "user", "lexagri200430@gmail.com"), new HashSet<>(), new UserDtoResponse(1L, "user", "lexagri200430@gmail.com")); // инициализация необходимыми данными

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(author));
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);
        when(userRepository.findById(taskRequest.assigneeId())).thenReturn(Optional.of(assignee));
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));
    }@Test

    public void testGetAllTasks() {
        // Подготовка данных
        Pageable pageable = mock(Pageable.class);
        Task task = new Task();
        when(taskRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(task)));

        // Вызов метода
        Page<TaskResponse> responses = taskService.getAllTasks(pageable);

        // Проверка результатов
        assertNotNull(responses);
        assertEquals(1L, responses.get().count());
        verify(taskMapper).toResponse(task);
    }

    @Test
    public void testCreateTask() {
        // Подготовка данных
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);

        // Вызов метода
        TaskResponse response = taskService.createTask(userDetails, taskRequest);

        // Проверка результатов
        assertNotNull(response);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testUpdateTask() {
        // Подготовка данных
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));


        // Вызов метода
        TaskResponse response = taskService.updateTask(userDetails, taskRequest, taskId);

        // Проверка результатов
        assertNotNull(response);
        verify(taskRepository).save(taskEntity);
    }

    @Test
    public void testDeleteTask_Success() {
        when(customUserDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(userDetails);
        // Подготовка данных
        Long taskId = 1L;
        Task task = new Task();
        task.setAssignee(author); // для теста
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Вызов метода
        TaskResponse response = taskService.deleteTask(userDetails, taskId);


        // Проверка результатов
        assertNotNull(response);
        verify(taskRepository).delete(task);
    }

    @Test
    public void testGetTaskById() {
        // Подготовка данных
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        // Вызов метода
        TaskResponse response = taskService.getTaskById(userDetails, taskId);

        // Проверка результатов
        assertNotNull(response);
        verify(taskMapper).toResponse(taskEntity);
    }

}