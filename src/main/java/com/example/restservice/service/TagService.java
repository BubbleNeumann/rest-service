//package com.example.restservice.service;
//
//import com.example.restservice.model.BaseEntity;
//import com.example.restservice.model.Game;
//import com.example.restservice.model.Tag;
//import com.example.restservice.repository.TagRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TagService implements IEntityService<Tag> {
//
//    @Autowired
//    TagRepository repo;
//
//    @Override
//    public BaseEntity getById(Long id) {
//        return repo.findById(id).orElse(null);
//    }
//
//    @Override
//    public void save(Game game) {
//        // TODO implement this
//    }
//
//    @Override
//    public void delete(Long id) {
//        // TODO implement this
//    }
//
//    @Override
//    public List<Tag> getAll() {
//        return repo.findAll();
//    }
//}
