package com.example.demo.mapping;

import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.UserDto;

public interface UserMapper {
    UserDto toDto(UserEntity user);
    UserEntity toEntity(UserDto user);
}
