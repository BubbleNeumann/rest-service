package com.example.restservice.service;

import com.example.restservice.model.BaseEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEntityService<T extends BaseEntity> {
    T getById(Long id);

    void save(T entity);

    void delete(Long id);

    List<T> getAll(Integer page);
}
