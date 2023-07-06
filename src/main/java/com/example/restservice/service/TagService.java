package com.example.restservice.service;

import com.example.restservice.dto.GameDTO;
import com.example.restservice.dto.TagDTO;
import com.example.restservice.model.Tag;
import com.example.restservice.model.Game;
import com.example.restservice.repository.GameRepo;
import com.example.restservice.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService implements IEntityService<TagDTO> {

    @Autowired
    TagRepository repo;

    @Autowired
    GameRepo gameRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TagDTO getById(Long id) {
        return modelMapper.map(repo.findById(id).orElse(null), TagDTO.class);
    }

    @Override
    public void save(TagDTO tagDTO) {
        repo.save(modelMapper.map(tagDTO, Tag.class));
    }

    @Override
    public void delete(Long id) {
        Tag tag = repo.findById(id).orElse(null);
        List<Game> games = getAllGames(id)
                .stream()
                .map((gameDTO) -> modelMapper.map(gameDTO, Game.class))
                .collect(Collectors.toList());
        for (Game game : games) {
            game.removeTag(tag);
            gameRepo.save(game);
        }
        repo.deleteById(id);
    }

    @Override
    public List<TagDTO> getAll(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        List<Tag> tags = repo.findAll(pageable).getContent();
        return tags.stream().map((tag) -> modelMapper.map(tag, TagDTO.class)).collect(Collectors.toList());
    }

    public List<GameDTO> getAllGames(Long tagId) {
        return repo.getAllGames(tagId)
                .stream()
                .map((game) -> modelMapper.map(game, GameDTO.class))
                .collect(Collectors.toList());
    }
}
