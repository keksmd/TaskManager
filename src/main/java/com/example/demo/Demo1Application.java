package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableWebSecurity
//@EnableSwagger2
//@EnableOpenApi
public class Demo1Application {

    public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}

}
