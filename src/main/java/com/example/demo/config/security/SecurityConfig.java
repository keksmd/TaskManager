package com.example.demo.config.security;

import com.example.demo.config.security.jwt.JwtRequestFilter;
import com.example.demo.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    JwtRequestFilter jwtRequestFilter(@Autowired @Qualifier("customUserDetailsService") CustomUserDetailsService customUserDetailsService){
        return new JwtRequestFilter(customUserDetailsService);
        
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,@Autowired JwtRequestFilter jwtTokenFilter) throws Exception {
        http.cors(c->{
                })
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((s) -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exHandler) -> exHandler.
                        authenticationEntryPoint(
                                (request, response, ex) -> {
                                    ex.printStackTrace();
                                    if (ex instanceof org.springframework.security.authentication.BadCredentialsException||
                                     ex instanceof UsernameNotFoundException) {
                                        response.sendError(
                                                HttpServletResponse.SC_UNAUTHORIZED,
                                                ex.getMessage()
                                        );
                                    }
                                }
                        ))
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/taskmaster/users/auth",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v2/api-docs",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/configuration/ui",
                                "/configuration/**",
                                "/csrf",
                                "/",
                                "/configuration/security"
                            ).permitAll()
                      .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtTokenFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .httpBasic(h->{});
        return http.build();
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http, @Autowired CustomUserDetailsService customUserDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

}