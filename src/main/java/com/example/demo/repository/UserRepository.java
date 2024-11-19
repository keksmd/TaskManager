package com.example.demo.repository;

import com.example.demo.model.data.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
   void deleteByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
}
