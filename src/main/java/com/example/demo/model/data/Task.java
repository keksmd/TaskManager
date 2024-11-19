package com.example.demo.data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Task {
    private String header;
    @Enumerated
    private Status status;
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;
    @OneToMany
    private Set<Comment> comments;

    @JoinColumn(name = "assingnee_id")
    @ManyToOne
    private UserEntity assignee;


}
