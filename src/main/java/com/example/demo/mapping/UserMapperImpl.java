package com.example.demo.mapping;

import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.RoleDto;
import com.example.demo.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper{

    @Override
    public UserDto toDto(UserEntity user) {
        return new UserDto(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getAuthorities()
                        .stream()
                        .map(e -> new RoleDto(e.getAuthority()))
                        .collect(Collectors.toSet()),user.getEmail());
    }

    @Override
    public UserEntity toEntity(UserDto user) {
        UserEntity  userEntity= new UserEntity();
        userEntity.setPassword(user.password());
        userEntity.setUsername(user.username());
        userEntity.setEmail(user.email());
        userEntity.setPasswordConfirm(user.passwordConfirm());
        return  userEntity;
    }
}
