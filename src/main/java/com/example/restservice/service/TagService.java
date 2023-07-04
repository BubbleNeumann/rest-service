package com.example.restservice.service;

import com.example.restservice.model.Tag;
import com.example.restservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService implements IEntityService<Tag> {

    @Autowired
    TagRepository repo;

    @Override
    public Tag getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void save(Tag tag) {
        repo.save(tag);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Tag> getAll(Integer page) {
        final int size = 5;
        PageRequest pageable = PageRequest.of(page - 1, size);
        return repo.findAll(pageable).getContent();
    }
}
