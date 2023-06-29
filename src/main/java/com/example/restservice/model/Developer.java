package com.example.restservice.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "developers")
public class Developer extends BaseEntity {
    private String name;
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "games", cascade = CascadeType.ALL)
//    private Set<Game> games;

//    public Set<Game> getGames() {
//        return games;
//    }

//    public void setGames(Set<Game> games) {
//        this.games = games;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
