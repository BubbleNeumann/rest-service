package com.example.restservice.service;

import com.example.restservice.model.Game;
import com.example.restservice.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService implements IEntityService {

    @Autowired
    GameRepository repo;
    @Override
    public Game getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void save(Game game) {
        repo.save(game);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Game> getAll() {
        return repo.findAll();
    }
}
