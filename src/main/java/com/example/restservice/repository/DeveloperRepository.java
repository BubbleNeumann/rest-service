package com.example.restservice.repository;

import com.example.restservice.model.Developer;
import com.example.restservice.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    @Query("from Game where dev.id = :devId")
    List<Game> getAllGames(Long devId);
}
