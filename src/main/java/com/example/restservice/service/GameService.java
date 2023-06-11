package com.example.restservice.service;

import com.example.restservice.model.Game;
import com.example.restservice.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService implements IEntityService {

    @Autowired
    GameRepository gameRepository;
    @Override
    public Game getById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Game game) {
        gameRepository.save(game);
    }

    @Override
    public void delete(Long id) {
        gameRepository.deleteById(id);
    }

    @Override
    public List<Game> getAll() {
        return gameRepository.findAll();
    }
}
