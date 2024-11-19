package com.example.demo.repository;

import com.example.demo.model.data.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository  extends JpaRepository<Task,Long>{

}
