package com.example.restservice.service;

import com.example.restservice.dto.DevDTO;
import com.example.restservice.dto.GameDTO;
import com.example.restservice.model.BaseEntity;
import com.example.restservice.model.Developer;
import com.example.restservice.model.Game;
import com.example.restservice.repository.DeveloperRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeveloperService implements IEntityService<DevDTO> {

    @Autowired
    private DeveloperRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DevDTO getById(Long id) {
        return castToDTO(repo.findById(id).orElse(null));
    }

    @Override
    public Long save(DevDTO devDTO) {
        Developer dev = repo.saveAndFlush(modelMapper.map(devDTO, Developer.class));
        return dev.getId();
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<DevDTO> getAll(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        List<Developer> devs = repo.findAll(pageable).getContent();
        return devs.stream().map(this::castToDTO).collect(Collectors.toList());
    }

    /**
     *
     * @param devId
     * @return
     * @throws RuntimeException if developer wasn't found by id.
     */
    public List<GameDTO> getAllGames(Long devId) throws RuntimeException {
        Developer dev = repo.findById(devId).orElse(null);
        if (dev == null) {
            throw new RuntimeException("dev wasn't found");
        }
        return dev.getGames()
                .stream()
                .map(this::castGameToDTO)
                .collect(Collectors.toList());
    }

    private GameDTO castGameToDTO(Game game) {
        GameDTO gameDTO = modelMapper.map(game, GameDTO.class);
        gameDTO.setTagIds(game.getTags().stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        return gameDTO;
    }

    private DevDTO castToDTO(Developer dev) {
        DevDTO devDTO = modelMapper.map(dev, DevDTO.class);
        devDTO.setGameIds(new HashSet<>(
                dev.getGames()
                        .stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toList())
        ));
        return devDTO;
    }
}
