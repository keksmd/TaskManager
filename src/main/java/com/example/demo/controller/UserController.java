package com.example.demo.controller;

import com.example.demo.model.dto.AuthDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/taskmaster/users")
public interface UserController {
    @PostMapping("/auth")


    @ResponseBody String auth(@RequestBody AuthDto auth);
}
