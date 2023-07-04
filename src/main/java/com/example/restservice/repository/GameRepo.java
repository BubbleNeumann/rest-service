package com.example.restservice.repository;

import com.example.restservice.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface GameRepo extends JpaRepository<Game, Long> {
//    List<Game> findAll(Pageable pages);
}
