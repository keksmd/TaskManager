package com.example.demo.config.security;

import com.example.demo.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;


public class JwtRequestFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService userDetailsService;

    public JwtRequestFilter(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken;
        String username;
        String bearerToken = request.getHeader("Authorization");
        System.out.println("beater: "+bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            jwtToken = bearerToken.substring(7);
            System.out.println("jwt:"+jwtToken);
            username = JwtUtil.extractUsername(jwtToken);
            System.out.println("username: "+username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null&&userDetailsService.userExists(username)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (JwtUtil.validateToken(jwtToken, userDetails)) {
                    try {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }catch (Exception e){
                        e.printStackTrace();
                        throw  e;
                    }
                }
            }
        }else if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Basic ")) {
            String base64Credentials = bearerToken.substring("Basic ".length()).trim();
            username = new String(Base64.getDecoder().decode(base64Credentials));
            String password = username.split(":")[1];
            username = username.split(":")[0];

            if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null&&userDetailsService.userExists(username)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(userDetails.getPassword().equals(password)) {
                    try {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

