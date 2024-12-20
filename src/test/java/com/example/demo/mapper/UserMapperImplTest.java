package com.example.demo.mapper;

import com.example.demo.mapping.UserMapperImpl;
import com.example.demo.model.data.Role;
import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.RoleDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.UserDtoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperImplTest {

    @InjectMocks
    private UserMapperImpl userMapper;

    private UserEntity userEntity;
    private UserDto userDto;
    Role role ;

    @BeforeEach
    public void setUp() {
        role = new Role() {

            @Override
            public String getAuthority() {
                return "role";
            }
        };
        userMapper = new UserMapperImpl();
        
        // Создание тестового UserEntity
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testUser");
        userEntity.setPassword("password123");
        userEntity.setEmail("test@example.com");

        userEntity.setRoles(new HashSet<>(List.of(role)));
    }

    @Test
    public void testToDto() {
        // Вызов метода
        UserDtoResponse dto = userMapper.toDto(userEntity);

        // Проверка результатов
        assertNotNull(dto);
        assertEquals(userEntity.getId(), dto.id());
        assertEquals(userEntity.getUsername(), dto.username());
        assertEquals(userEntity.getEmail(), dto.email());

    }

    @Test
    public void testToEntity() {

        userDto = new UserDto(1L, "testUser", "password123", "test@example.com",new HashSet<>(List.of(new RoleDto("role"))), "test@example.com");
        
        UserEntity entity = userMapper.toEntity(userDto);
        
        assertNotNull(entity);
        assertEquals(userDto.username(), entity.getUsername());
        assertEquals(userDto.email(), entity.getEmail());

    }
}
