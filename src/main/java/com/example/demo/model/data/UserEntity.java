package com.example.demo.model.data;

import jakarta.persistence.*;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Data
@Entity
public class UserEntity implements UserDetails {
    private String email;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    @Transient
    private String passwordConfirm;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }
    @OneToMany
    private  Set<Task> tasks = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity entity)) return false;
        return Objects.equals(email, entity.email) && Objects.equals(id, entity.id) && Objects.equals(username, entity.username) && Objects.equals(password, entity.password) && Objects.equals(passwordConfirm, entity.passwordConfirm) && Objects.equals(roles, entity.roles) && Objects.equals(tasks, entity.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, id, username, password, passwordConfirm);
    }
}
