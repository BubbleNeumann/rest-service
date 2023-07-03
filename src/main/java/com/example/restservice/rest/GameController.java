package com.example.restservice.rest;


import com.example.restservice.dto.GameDTO;
import com.example.restservice.dto.TagDTO;
import com.example.restservice.model.Game;
import com.example.restservice.model.Tag;
import com.example.restservice.service.DeveloperService;
import com.example.restservice.service.GameService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
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
@RequestMapping("/api/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private DeveloperService devService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> getGame(@PathVariable("id") Long gameId) {
        if (gameId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Game game = this.gameService.getById(gameId);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(modelMapper.map(game, GameDTO.class), HttpStatus.OK);
    }

    /**
     * Creates a new entry in the games table. Checks for dev_id in the developers table,
     * returns FAILED_DEPENDENCY status if developer with corresponding id wasn't found.
     * Fetches Developer object from db based on gameDTO's devId.
     */
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> saveGame(@RequestBody GameDTO gameDTO) {
        if (gameDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        // check if developer exists
        if (devService.getById(gameDTO.getDevId()) == null) {
            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        }
        Game game = modelMapper.map(gameDTO, Game.class);
        game.setDev(devService.getById(gameDTO.getDevId()));
        this.gameService.save(game);
        return new ResponseEntity<>(gameDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> deleteGame(@PathVariable("id") Long id) {
        Game game = this.gameService.getById(id);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.gameService.delete(id);
        return new ResponseEntity<>(modelMapper.map(game, GameDTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDTO>> getAll() {
        List<Game> games = this.gameService.getAll();
        if (games.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(games.stream().map((game) -> modelMapper.map(game, GameDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * @param id - game id.
     * @return all tags applied to the game with given id.
     */
    @RequestMapping(value = "{id}/tags", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagDTO>> getAllTags(@PathVariable("id") Long id) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from Tag as t inner join t.games as g where g.id = :id");
            query.setParameter("id", id);
            List<Tag> tags = query.list();
            session.getTransaction().commit();
            return new ResponseEntity<>(tags.stream().map((game) -> modelMapper.map(game, TagDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
