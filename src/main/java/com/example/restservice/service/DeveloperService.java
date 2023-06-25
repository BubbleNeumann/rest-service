package com.example.restservice.service;

import com.example.restservice.model.BaseEntity;
import com.example.restservice.model.Developer;
import com.example.restservice.model.Game;
import com.example.restservice.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeveloperService implements IEntityService<Developer> {

    @Autowired
    DeveloperRepository repo;
    @Override
    public Developer getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void save(Developer dev) {
        repo.save(dev);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Developer> getAll() {
        return null;
    }
}
