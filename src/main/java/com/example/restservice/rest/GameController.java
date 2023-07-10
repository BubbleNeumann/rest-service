package com.example.restservice.rest;


import com.example.restservice.dto.GameDTO;
import com.example.restservice.dto.TagDTO;
import com.example.restservice.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "id={id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> getGame(@PathVariable("id") Long gameId) {
        if (gameId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        GameDTO gameDTO = this.gameService.getById(gameId);
        if (gameDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }

    /**
     * Creates a new entry in the games table. Checks for dev_id in the developers table,
     * returns FAILED_DEPENDENCY status if developer  or one of the tags with corresponding id wasn't found.
     */
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> saveGame(@RequestBody GameDTO gameDTO) {
        if (gameDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        try {
            gameDTO.setId(this.gameService.save(gameDTO));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        }
        return new ResponseEntity<>(gameDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> updateGame(@RequestBody GameDTO gameDTO) {
        if (gameDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        this.gameService.save(gameDTO);
        return new ResponseEntity<>(gameDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "id={id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> deleteGame(@PathVariable("id") Long id) {
        GameDTO gameDTO = this.gameService.getById(id);
        if (gameDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.gameService.delete(id);
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }

    /**
     *
     * @param pageNum page number to return. Defaults to 1 if null
     * @param size number of elements on one page. Defaults to 1 if null
     * @return
     */
    @RequestMapping(
            value = {
                    "page={page}/size={size}/sort={sort}/prop={prop}",
                    "size={size}/sort={sort}/prop={prop}",
                    "page={page}",
                    ""
            },
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<GameDTO>> getAll(
            @PathVariable(value = "page", required = false) Integer pageNum,
            @PathVariable(value = "size", required = false) Integer size,
            @PathVariable(value = "sort", required = false) String sort,
            @PathVariable(value = "prop", required = false) String prop
    ) {
        if (sort != null) {
            Sort.Direction dir;
            if (sort.equals("asc")) {
                dir = Sort.Direction.ASC;
            } else if (sort.equals("desc")) {
                dir = Sort.Direction.DESC;
            } else {
                // sort option isn't correct
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            try {
                List<GameDTO> gameDTOs = this.gameService.getAll(
                        pageNum == null ? 1 : pageNum,
                        size == null ? 1 : size,
                        Sort.by(dir, prop)
                );
                if (gameDTOs.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(gameDTOs, HttpStatus.OK);
            } catch (Exception e) {
                // props aren't correct
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        List<GameDTO> gameDTOs = this.gameService.getAll(
                pageNum == null ? 1 : pageNum,
                size == null ? 1 : size
        );
        if (gameDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gameDTOs, HttpStatus.OK);
    }

    /**
     * Fetches all tags of the game.
     *
     * @param id - game id.
     * @return all tags applied to the game with given id.
     */
    @RequestMapping(
            value = "id={id}/tags",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<TagDTO>> getAllTags(@PathVariable("id") Long id) {
        try {
            List<TagDTO> tagDTOs = this.gameService.getAllTags(id);
            if (tagDTOs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
