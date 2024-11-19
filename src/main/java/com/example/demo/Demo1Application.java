package com.example.demo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableWebSecurity
public class Demo1Application {

    public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}

}
