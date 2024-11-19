package com.example.demo.dto;

import java.util.Set;

public record UserDto(Long id,
                      String username,
                      String password,
                      String passwordConfirm,
                      Set<RoleDto> roles) {
}
