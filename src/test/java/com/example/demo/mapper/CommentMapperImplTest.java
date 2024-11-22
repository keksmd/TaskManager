package com.example.demo.mapper;

import com.example.demo.mapping.CommentMapperImpl;
import com.example.demo.mapping.UserMapper;
import com.example.demo.model.data.Comment;
import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.CommentDtoRequest;
import com.example.demo.model.dto.CommentDtoResponse;
import com.example.demo.model.dto.UserDtoResponse;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class CommentMapperImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private UserMapper userMapper;
    UserEntity author = new UserEntity();

    @InjectMocks
    private CommentMapperImpl commentMapper;

    private Comment comment;
    private CommentDtoRequest commentDtoRequest;

    @BeforeEach
    public void setUp() {
        openMocks(this);


        // Создание тестового комментария
        comment = new Comment();
        comment.setId(1L);
        comment.setBody("This is a comment");
        Comment parentComment = new Comment()  ;
        parentComment.setId(4L);
        comment.setParent(parentComment);
        author.setId(1L);
        comment.setAuthor(author);
        commentDtoRequest = new CommentDtoRequest(4L,"This is a comment");

        
        // Создание тестового ответа

    }

    @Test
    public void testToResponse() {
        when(userMapper.toDto(any(UserEntity.class))).thenReturn(new UserDtoResponse(1L,"admin","lexagri220430@gmail.com"));
        CommentDtoResponse response = commentMapper.toResponse(comment);
        // Проверка результатов
        assertNotNull(response);
        assertEquals("This is a comment", response.body());
        assertEquals(1L, response.id());
    }



    @Test

    public void testToEntityWithNullReferences() {
        commentDtoRequest = new CommentDtoRequest(null,"This is a comment");


        // Вызов метода
        Comment entity = commentMapper.toEntity(commentDtoRequest,null);

        // Проверка результатов
        assertNotNull(entity);
        assertEquals("This is a comment", entity.getBody());
        assertNull(entity.getParent());
        assertNull(entity.getAuthor());
    }
}

