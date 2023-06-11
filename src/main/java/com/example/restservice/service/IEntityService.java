package com.example.restservice.service;

import com.example.restservice.model.BaseEntity;
import com.example.restservice.model.Game;

import java.util.List;

public interface IEntityService<T extends BaseEntity> {
    BaseEntity getById(Long id);
    void save(Game game);
    void delete(Long id);

    List<T> getAll();
}
