package com.example.demo.aspect;

import com.example.demo.annotation.Roles;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class UserAccessAspect {
    @Before(value = "@annotation(allowedRoles)")
    public Object checkUserAccess(ProceedingJoinPoint joinPoint, Roles allowedRoles) throws Throwable {
        System.out.println("roles from annotation : "+ Arrays.toString(allowedRoles.allow()));
        System.out.println("userDetails from args: "+ Arrays.toString(joinPoint.getArgs()));


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Получение пользователя и его ролей
            UserDetails userDetailsFromContext = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetailsFromContext);
            Set<String> roles = userDetailsFromContext.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            List<String> allowwedRolesLowerCase = Arrays.stream(allowedRoles.allow())
                    .map(String::toLowerCase)
                    .toList();

            if (roles.stream()
                    .anyMatch(allowwedRolesLowerCase::contains)) {
                return joinPoint.proceed();
            }
        }

        throw new SecurityException("Access Denied!"); // Исключение, если доступ запрещен
    }

}
