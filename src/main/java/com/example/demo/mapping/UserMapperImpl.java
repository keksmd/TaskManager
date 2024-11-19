package com.example.demo.mapping;

import com.example.demo.model.data.UserEntity;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.UserDtoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper{

    @Override
    public UserDtoResponse toDto(UserEntity user) {
        if(user==null){
            return new UserDtoResponse(null,"","");
        }
        return new UserDtoResponse(user.getId(),
                user.getUsername(),
              user.getEmail());
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
