package com.example.restservice.rest;

import com.example.restservice.dto.DevDTO;
import com.example.restservice.model.Developer;
import com.example.restservice.service.DeveloperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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
            // TODO redirect to getAll()
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Developer dev = this.devService.getById(devId);
        if (dev == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DevDTO devDTO = modelMapper.map(dev, DevDTO.class);
        return new ResponseEntity<>(devDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DevDTO> saveDeveloper(@RequestBody DevDTO devDTO) {
        if (devDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        Developer dev = modelMapper.map(devDTO, Developer.class);
        this.devService.save(dev);
        return new ResponseEntity<>(devDTO, headers, HttpStatus.CREATED);
    }

}
