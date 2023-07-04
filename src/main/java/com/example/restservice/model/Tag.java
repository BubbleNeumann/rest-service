package com.example.restservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag extends BaseEntity {
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "tags")
    private Set<Game> games;

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + this.getId() + '\'' +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(this.getId(), tag.getId()) && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId()) + Objects.hash(name);
    }
}
