package com.example.demo.service;

import com.example.demo.model.data.Role;
import com.example.demo.model.data.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service// Аннотация для логирования с помощью Lombok
public class CustomUserDetailsService implements UserDetailsManager {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void customUserDetailsService() {
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("admin");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("user");
        roleRepository.save(userRole);


        var admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword("admin");
        adminRole.setUsers(new HashSet<>(List.of(admin)));
        admin.setRoles(new HashSet<>(List.of(adminRole, userRole)));
        roleRepository.save(adminRole);
        this.createUser(admin);

        var user = new UserEntity();
        user.setUsername("user");
        user.setPassword("user");
        userRole.setUsers(new HashSet<>(List.of(user, admin)));
        user.setRoles(new HashSet<>(List.of(userRole)));
        roleRepository.save(userRole);
        this.createUser(user);

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });
    }

    @Override
    public void createUser(UserDetails user) {
        log.info("Creating user: {}", user.getUsername());
        UserEntity entity = (UserEntity) user;
        userRepository.save(entity);
        log.info("User created: {}", user.getUsername());
    }

    @Override
    public void updateUser(UserDetails user) {
        log.info("Updating user: {}", user.getUsername());
        UserEntity entity = (UserEntity) user;
        userRepository.save(entity);
        log.info("User updated: {}", user.getUsername());
    }

    @Override
    public void deleteUser(String username) {
        log.info("Deleting user: {}", username);
        userRepository.deleteByUsername(username);
        log.info("User deleted: {}", username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        log.info("Changing password for user with old password: {}", oldPassword);
        userRepository.findAll().stream()
                .filter(u -> u.getPassword().equals(oldPassword))
                .forEach(u -> {
                    u.setPassword(newPassword);
                    log.info("Password changed for user: {}", u.getUsername());
                });
    }

    @Override
    public boolean userExists(String username) {
        log.info("Checking existence of user: {}", username);
        try {
            UserDetails userDetails = this.loadUserByUsername(username);
            boolean exists = userDetails.isEnabled();
            log.info("User exists: {}", username);
            return exists;
        } catch (UsernameNotFoundException e) {
            log.warn("User not found: {}", username);
            return false;
        }
    }

    private UserEntity findByDetails(UserDetails userDetails) {
        log.info("Finding user by details for username: {}", userDetails.getUsername());
        if (this.userExists(userDetails.getUsername())) {
            return (UserEntity) this.loadUserByUsername(userDetails.getUsername());
        } else {

            log.warn("User not found with username: {}", userDetails.getUsername());
            throw new UsernameNotFoundException("User not found with username: " + userDetails.getUsername());
        }
    }

    public boolean isAdminOrAssignee(UserDetails userDetails, Long taskId) {
        log.info("Checking if user is admin or assignee for task ID: {}", taskId);
        try {
            var user = this.findByDetails(userDetails);
            boolean assignee;
            try {
                assignee = Objects.requireNonNull(taskRepository.findById(taskId).orElse(null)).getAssignee().getId().equals(user.getId());
            } catch (NullPointerException e) {
                assignee = false;
            }
            boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getAuthority().equalsIgnoreCase("admin"));
            boolean result = isAdmin || assignee;
            log.info("User {} is {} for task ID: {}", user.getUsername(), result ? "admin or assignee" : "not admin or assignee", taskId);
            return result;
        } catch (UsernameNotFoundException e) {
            log.warn("Username not found when checking permissions for task ID: {}", taskId);
            return false;
        }
    }

    public boolean isAdmin(UserDetails userDetails) {
        log.info("Checking if user is admin for username: {}", userDetails.getUsername());
        try {
            var user = this.findByDetails(userDetails);
            boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getAuthority().equalsIgnoreCase("admin"));
            log.info("User {} is {}", user.getUsername(), isAdmin ? "an admin" : "not an admin");
            return isAdmin;
        } catch (UsernameNotFoundException e) {
            log.warn("Username not found when checking admin status: {}", userDetails.getUsername());
            return false;
        }
    }
}
