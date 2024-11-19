package com.example.demo.model.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;
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
    @ManyToOne
    private UserEntity author;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(parent, comment.parent) && Objects.equals(children, comment.children) && Objects.equals(id, comment.id) && Objects.equals(body, comment.body) && Objects.equals(author, comment.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, body);
    }
}