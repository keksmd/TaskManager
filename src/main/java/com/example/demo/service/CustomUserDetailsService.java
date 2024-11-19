package com.example.demo.service;

import com.example.demo.model.data.UserEntity;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Objects;


@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsManager {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public void createUser(UserDetails user) {
        UserEntity entity = (UserEntity) user;
        userRepository.save(entity);
    }

    @Override
    public void updateUser(UserDetails user) {
        UserEntity entity = (UserEntity) user;
        userRepository.save(entity);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        userRepository.findAll().stream().filter(u->u.getPassword().equals(oldPassword)).forEach(u->u.setPassword(newPassword));
    }

    @Override
    public boolean userExists(String username) {
        try {
            UserDetails userDetails = this.loadUserByUsername(username);
            return userDetails.isEnabled();
        }catch (UsernameNotFoundException e){
            return false;
        }
    }
    private UserEntity findByDetails(UserDetails userDetails){
        if(this.userExists(userDetails.getUsername())){
            return (UserEntity) this.loadUserByUsername(userDetails.getUsername());
        }else{
            throw new  UsernameNotFoundException("User not found with username: " + userDetails.getUsername());
        }
    }
    private final TaskRepository taskRepository;
    public boolean isAdminOrAssigneeOfTaskAnd(UserDetails userDetails,Long taskId){
        try {
            var user = this.findByDetails(userDetails);
            boolean assignee;
            try {
                 assignee= Objects.requireNonNull(taskRepository.findById(taskId).orElse(null)).getAssignee().getId().equals(user.getId());
            }catch (NullPointerException e){
                assignee = false;
            }
                return (user.getRoles().stream().anyMatch(r -> r.getAuthority().equalsIgnoreCase("admin"))
                        ||assignee);
            }
        catch (UsernameNotFoundException e){
            return  false;
        }

    }
    public boolean isAdmin(UserDetails userDetails){
        try {
            var user = this.findByDetails(userDetails);
            return (user.getRoles().stream().anyMatch(r-> r.getAuthority().equalsIgnoreCase("admin")));
        }catch (UsernameNotFoundException e){
            return  false;
        }

        }

}