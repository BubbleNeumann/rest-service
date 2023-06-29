package com.example.restservice.rest;

import com.example.restservice.dto.DevDTO;
import com.example.restservice.dto.GameDTO;
import com.example.restservice.model.Developer;
import com.example.restservice.service.DeveloperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {
    @Autowired
    private DeveloperService devService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DevDTO> getDeveloper(@PathVariable("id") Long devId) {
        if (devId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Developer dev = this.devService.getById(devId);
        if (dev == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(modelMapper.map(dev, DevDTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DevDTO> saveDeveloper(@RequestBody DevDTO devDTO) {
        if (devDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        this.devService.save(modelMapper.map(devDTO, Developer.class));
        return new ResponseEntity<>(devDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DevDTO> deleteDeveloper(@PathVariable("id") Long id) {
        Developer dev = this.devService.getById(id);
        if (dev == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.devService.delete(id);
        return new ResponseEntity<>(modelMapper.map(dev, DevDTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DevDTO>> getAll() {
        List<Developer> devs = this.devService.getAll();
        if (devs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(devs.stream().map((dev) -> modelMapper.map(dev, DevDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}/games", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDTO>> getAllGames(@PathVariable("id") Long id) {
        //TODO implement this
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
