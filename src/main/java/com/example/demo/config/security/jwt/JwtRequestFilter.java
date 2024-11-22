package com.example.demo.config.security.jwt;

import com.example.demo.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j; // Импортируем аннотацию для логирования
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Slf4j // Аннотация для логирования
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

        log.info("Authorization header: {}", bearerToken);

        // Проверка на Bearer Token
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            jwtToken = bearerToken.substring(7);
            log.info("Extracted JWT Token: {}", jwtToken);
            username = JwtUtil.extractUsername(jwtToken);
            log.info("Extracted username: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && userDetailsService.userExists(username)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (JwtUtil.validateToken(jwtToken, userDetails)) {
                    try {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        log.info("User {} authenticated successfully with Bearer token", username);
                    } catch (Exception e) {
                        log.error("Authentication error for user {}: {}", username, e.getMessage());
                        throw e;
                    }
                }
            }
        }
        // Проверка на Basic Authentication
        else if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Basic ")) {
            String base64Credentials = bearerToken.substring("Basic ".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            username = credentials.split(":")[0];

            String password = credentials.split(":")[1];

            log.info("Extracted Basic credentials for user: {}", username);

            if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null && userDetailsService.userExists(username)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (userDetails.getPassword().equals(password)) {
                    try {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        log.info("User {} authenticated successfully with Basic token", username);
                    } catch (Exception e) {
                        log.error("Authentication error for user {}: {}", username, e.getMessage());
                        throw e;
                    }
                } else {
                    log.warn("Invalid password for user: {}", username);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
