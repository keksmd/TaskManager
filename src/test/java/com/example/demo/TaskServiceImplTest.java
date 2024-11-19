package com.example.demo;

import com.example.demo.exception.Forbidden;
import com.example.demo.mapping.TaskMapper;
import com.example.demo.model.Priority;
import com.example.demo.model.Status;
import com.example.demo.model.data.Task;
import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.TaskRequest;
import com.example.demo.model.dto.TaskResponse;
import com.example.demo.model.dto.UserDto;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private TaskResponse taskResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создание тестовых данных
        userDetails = mock(UserDetails.class);
        taskRequest = new TaskRequest(Priority.HIGH, "testTask", Status.IN_REVIEW,null,1L);
        taskEntity = new Task();
        author = new UserEntity();
        taskResponse = new TaskResponse(Priority.HIGH, "testTask", Status.IN_REVIEW,null,null,null,new UserDto(1L,"user","pswd","pswd2",null,"lexagri200430@gmail.com")); // инициализация необходимыми данными

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(author));
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);
    }

    @Test
    public void testGetAllTasks() {
        // Подготовка данных
        Pageable pageable = mock(Pageable.class);
        Task task = new Task();
        when(taskRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(task)));

        // Вызов метода
        List<TaskResponse> responses = taskService.getAllTasks(pageable);

        // Проверка результатов
        assertNotNull(responses);
        assertEquals(1, responses.size());
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
        when(taskRepository.getReferenceById(taskId)).thenReturn(taskEntity);

        // Вызов метода
        TaskResponse response = taskService.updateTask(userDetails,  taskRequest, taskId);

        // Проверка результатов
        assertNotNull(response);
        verify(taskRepository).save(taskEntity);
    }

    @Test
    public void testDeleteTask_Success() throws ChangeSetPersister.NotFoundException, Forbidden {
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
        when(taskRepository.getReferenceById(taskId)).thenReturn(taskEntity);

        // Вызов метода
        TaskResponse response = taskService.getTaskById(userDetails, taskId);

        // Проверка результатов
        assertNotNull(response);
        verify(taskMapper).toResponse(taskEntity);
    }

    @Test
    public void testDeleteTask_Forbidden() {
        // Подготовка данных
        Long taskId = 1L;
        Task task = new Task();
        UserEntity assignee = new UserEntity();
        assignee.setUsername("otherUser"); // Пользователь, который не является текущим пользователем
        task.setAssignee(assignee);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Проверка, что метод выбрасывает Forbidden
        assertThrows(Forbidden.class, () -> taskService.deleteTask(userDetails, taskId));
    }
}
