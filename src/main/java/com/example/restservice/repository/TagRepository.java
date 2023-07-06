package com.example.restservice.repository;

import com.example.restservice.model.Game;
import com.example.restservice.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("from Game as g inner join g.tags as t where t.id = :tagId")
    List<Game> getAllGames(Long tagId);
}
