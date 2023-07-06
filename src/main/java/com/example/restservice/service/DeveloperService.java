package com.example.restservice.service;

import com.example.restservice.dto.DevDTO;
import com.example.restservice.dto.GameDTO;
import com.example.restservice.model.Developer;
import com.example.restservice.repository.DeveloperRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeveloperService implements IEntityService<DevDTO> {

    @Autowired
    DeveloperRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DevDTO getById(Long id) {
        return modelMapper.map(repo.findById(id).orElse(null), DevDTO.class);
    }

    @Override
    public void save(DevDTO devDTO) {
        repo.save(modelMapper.map(devDTO, Developer.class));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<DevDTO> getAll(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        List<Developer> devs = repo.findAll(pageable).getContent();
        return devs.stream().map((dev) -> modelMapper.map(dev, DevDTO.class)).collect(Collectors.toList());
    }

    public List<GameDTO> getAllGames(Long devId) {
        return repo.getAllGames(devId)
                .stream()
                .map((game) -> modelMapper.map(game, GameDTO.class))
                .collect(Collectors.toList());
    }
}
