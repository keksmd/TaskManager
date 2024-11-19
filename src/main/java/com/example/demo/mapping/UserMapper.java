package com.example.demo.mapping;

import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.UserDtoResponse;

public interface UserMapper {
    UserDtoResponse toDto(UserEntity user);
    UserEntity toEntity(UserDto user);
}
