package com.example.demo.security;

import com.example.demo.model.data.Role;
import com.example.demo.model.data.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.HashSet;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    CustomUserDetailsService customUserDetailsService(@Autowired UserRepository userRepository,
                                                      @Qualifier("adminRole") Role adminRole,
                                                      @Qualifier("userRole") Role userRole) {
        var userDetailsManager = new CustomUserDetailsService(userRepository);

        var user = new UserEntity();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("user");
        user.setRoles(new HashSet<>(List.of(userRole)));
        userRole.setUsers(new HashSet<>(List.of(user)));
        userDetailsManager.createUser(user);

        var admin = new UserEntity();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRoles(new HashSet<>(List.of(adminRole,userRole)));
        adminRole.setUsers(new HashSet<>(List.of(admin)));
        userDetailsManager.createUser(admin);
        return userDetailsManager;
    }

    @Bean
    Role adminRole(@Autowired RoleRepository roleRepository) {
        Role role = new Role();
        role.setId(1L);
        role.setName("admin");
        roleRepository.save(role);
        return role;
    }

    @Bean
    Role userRole(@Autowired RoleRepository roleRepository) {
        Role role = new Role();
        role.setId(2L);
        role.setName("user");
        roleRepository.save(role);
        return role;
    }
    @Bean
    JwtRequestFilter jwtRequestFilter(@Autowired @Qualifier("customUserDetailsService") CustomUserDetailsService customUserDetailsService){
        return new JwtRequestFilter(customUserDetailsService);
        
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,@Autowired JwtRequestFilter jwtTokenFilter,@Autowired CorsFilter corsFilter) throws Exception {
        http.cors(c->{
                })
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((s) -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exHandler) -> exHandler.
                        authenticationEntryPoint(
                                (request, response, ex) -> {
                                    ex.printStackTrace();
                                    response.sendError(
                                            HttpServletResponse.SC_UNAUTHORIZED,
                                            ex.getMessage()
                                    );
                                }
                        ))
                /*.addFilterBefore(
                        jwtTokenFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                 */
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/taskmaster/users/auth").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
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

}