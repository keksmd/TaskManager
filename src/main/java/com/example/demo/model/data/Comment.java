package com.example.demo.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

@Entity
@Data
public class Comment {
    @ManyToOne
    private Comment parent = null;
    @OneToMany
    private Set<Comment> children;
    @Getter
    @Id
    private Long id;
    private  String body;

}
