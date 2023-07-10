package com.example.restservice.service;

import com.example.restservice.dto.GameDTO;
import com.example.restservice.dto.TagDTO;
import com.example.restservice.model.BaseEntity;
import com.example.restservice.model.Developer;
import com.example.restservice.model.Game;
import com.example.restservice.model.Tag;
import com.example.restservice.repository.GameRepo;
import org.hibernate.service.internal.ServiceDependencyException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameService implements IEntityService<GameDTO> {

    @Autowired
    GameRepo repo;

    @Autowired
    private DeveloperService devService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GameDTO getById(Long id) {
        return castToDTO(repo.findById(id).orElse(null));
    }

    /**
     * Saves game object to db.
     * Fetches Developer and Tag objects from db based on gameDTO's devId and tagIds.
     * @param gameDTO - object to parse.
     * @throws RuntimeException in case developer or one of the tags wasn't found in the db.
     */
    @Override
    public Long save(GameDTO gameDTO) throws RuntimeException {
        Game game = modelMapper.map(gameDTO, Game.class);
        // check if developer exists
        if (devService.getById(gameDTO.getDevId()) == null) {
            throw new RuntimeException("dev wasn't found");
        }
        game.setDev(modelMapper.map(devService.getById(gameDTO.getDevId()), Developer.class));
        // check that all tag ids are valid and already exist
        Set<Long> tagIds = gameDTO.getTagIds();
        for (Long id : tagIds) {
            if (tagService.getById(id) == null) {
                throw new RuntimeException("tag" + id.toString() + "wasn't found");
            }
        }
        game.setTags(tagIds.stream().map((id) -> modelMapper.map(this.tagService.getById(id), Tag.class)).collect(Collectors.toSet()));
        game = repo.saveAndFlush(game);
        return game.getId();
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<GameDTO> getAll(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        List<Game> games = repo.findAll(pageable).getContent();
        return games.stream().map(this::castToDTO).collect(Collectors.toList());
    }

    public List<GameDTO> getAll(Integer page, Integer size, Sort sort) {
        PageRequest pageable = PageRequest.of(page - 1, size, sort);
        List<Game> games = repo.findAll(pageable).getContent();
        return games.stream().map(this::castToDTO).collect(Collectors.toList());
    }

    /**
     *
     * @param gameId
     * @throws RuntimeException if game with given id wasn't found in the db.
     */
    public List<TagDTO> getAllTags(Long gameId) throws RuntimeException{
        Game game = repo.findById(gameId).orElse(null);
        if (game == null) {
            throw new RuntimeException("game wasn't found");
        }
        Set<Tag> tags = game.getTags();
        return tags.stream().map((tag) -> modelMapper.map(tag, TagDTO.class)).collect(Collectors.toList());
    }

    private GameDTO castToDTO(Game game) {
        GameDTO gameDTO = modelMapper.map(game, GameDTO.class);
        // convert tag objects into tag ids, so we pass less data
        gameDTO.setTagIds(game.getTags().stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        return gameDTO;
    }
}
