package com.example.restservice.repository;

import com.example.restservice.model.Game;
import com.example.restservice.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepo extends JpaRepository<Game, Long> {
    @Query("from Tag as t inner join t.games as g where g.id = :gameId")
    List<Tag> getAllTags(Long gameId);
}
