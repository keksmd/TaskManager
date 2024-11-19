package com.example.demo.model.data;

import com.example.demo.model.Priority;
import com.example.demo.model.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Task {
    private String header;
    @Enumerated
    private Status status;
    @Enumerated
    private Priority priority;
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;
    @OneToMany
    private Set<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(header, task.header) && status == task.status && priority == task.priority && Objects.equals(id, task.id) && Objects.equals(author, task.author) && Objects.equals(comments, task.comments) && Objects.equals(assignee, task.assignee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, status, priority, id);
    }

    @JoinColumn(name = "assingnee_id")
    @ManyToOne
    private UserEntity assignee;


}
