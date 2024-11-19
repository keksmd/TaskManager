package com.example.demo.controller;

import com.example.demo.model.dto.AuthDto;
import com.example.demo.config.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserDetailsManager customUserDetailsService;

    @Override
    public  String auth( AuthDto auth) {
        String password = auth.password();
        String username = auth.username();
        if (customUserDetailsService.userExists(username)) {

            UserDetails details = customUserDetailsService.loadUserByUsername(username);
            if (details.getPassword().equals(password)){
                return JwtUtil.generateToken(details);
            }else{
                throw new CompromisedPasswordException("Password is compromised");
            }

        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

}
