package com.example.restservice.rest;

import com.example.restservice.dto.DevDTO;
import com.example.restservice.dto.GameDTO;
import com.example.restservice.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {
    @Autowired
    private DeveloperService devService;

    @RequestMapping(value = "id={id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DevDTO> getDeveloper(@PathVariable("id") Long devId) {
        if (devId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DevDTO devDTO = this.devService.getById(devId);
        if (devDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(devDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DevDTO> saveDeveloper(@RequestBody DevDTO devDTO) {
        if (devDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        this.devService.save(devDTO);
        return new ResponseEntity<>(devDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "id={id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DevDTO> deleteDeveloper(@PathVariable("id") Long id) {
        DevDTO devDTO = this.devService.getById(id);
        if (devDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.devService.delete(id);
        return new ResponseEntity<>(devDTO, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"", "page={page}", "size={size}", "page={page}/size={size}"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<DevDTO>> getAll(
            @PathVariable(value = "page", required = false) Integer pageNum,
            @PathVariable(value = "size", required = false) Integer size
    ) {
        List<DevDTO> devDTOs = this.devService.getAll(
                pageNum == null ? 1 : pageNum,
                size == null ? 1 : size
        );
        if (devDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(devDTOs, HttpStatus.OK);
    }

    /**
     * @param id - developer id.
     * @return all games by a developer if any were found.
     */
    @RequestMapping(value = "id={id}/games", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDTO>> getAllGames(@PathVariable("id") Long id) {
        List<GameDTO> games = this.devService.getAllGames(id);
        if (games.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
