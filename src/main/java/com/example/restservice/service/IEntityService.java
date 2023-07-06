package com.example.restservice.service;

import com.example.restservice.dto.BaseDTO;

import java.util.List;

public interface IEntityService<T extends BaseDTO> {
    T getById(Long id);

    void save(T entity);

    void delete(Long id);

    List<T> getAll(Integer page, Integer size);
}
