package com.example.restservice.rest;


import com.example.restservice.dto.GameDTO;
import com.example.restservice.dto.TagDTO;
import com.example.restservice.model.BaseEntity;
import com.example.restservice.model.Game;
import com.example.restservice.model.Tag;
import com.example.restservice.service.DeveloperService;
import com.example.restservice.service.GameService;
import com.example.restservice.service.TagService;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private DeveloperService devService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "id={id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> getGame(@PathVariable("id") Long gameId) {
        if (gameId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Game game = this.gameService.getById(gameId);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(castToDTO(game), HttpStatus.OK);
    }

    /**
     * Creates a new entry in the games table. Checks for dev_id in the developers table,
     * returns FAILED_DEPENDENCY status if developer  or one of the tags with corresponding id wasn't found.
     * Fetches Developer and Tag objects from db based on gameDTO's devId and tagIds.
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
        // check that all tag ids are valid and already exist
        Set<Long> tagIds = gameDTO.getTagIds();
        for (Long id : tagIds) {
            if (tagService.getById(id) == null) {
                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
            }
        }
        game.setTags(tagIds.stream().map((id) -> this.tagService.getById(id)).collect(Collectors.toSet()));
        this.gameService.save(game);
        return new ResponseEntity<>(gameDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> updateGame(@RequestBody GameDTO gameDTO) {
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
        // check that all tag ids are valid and already exist
        Set<Long> tagIds = gameDTO.getTagIds();
        for (Long id : tagIds) {
            if (tagService.getById(id) == null) {
                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
            }
        }
        game.setTags(tagIds.stream().map((id) -> this.tagService.getById(id)).collect(Collectors.toSet()));
        this.gameService.save(game);
        return new ResponseEntity<>(gameDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "id={id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> deleteGame(@PathVariable("id") Long id) {
        Game game = this.gameService.getById(id);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.gameService.delete(id);
        return new ResponseEntity<>(castToDTO(game), HttpStatus.OK);
    }

    /**
     * Endpoint for games.
     *
     * @param pageNum
     * @return 5 db entries from ((pageNum - 1) * 5 + 1) to (pageNum * 5) inclusive.
     */
    @RequestMapping(value = {"{page}", ""}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDTO>> getAll(@PathVariable(value = "page", required = false) Integer pageNum) {
        // if the page number wasn't specified then just return the first page
        System.out.println(pageNum);
        List<Game> games = this.gameService.getAll(pageNum == null ? 1 : pageNum);
        if (games.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<GameDTO> gameDTOS = games.stream().map((this::castToDTO)).collect(Collectors.toList());
        return new ResponseEntity<>(gameDTOS, HttpStatus.OK);
    }

    /**
     * Fetches all tags of the game.
     *
     * @param id - game id.
     * @return all tags applied to the game with given id.
     */
    @RequestMapping(value = "id={id}/tags", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagDTO>> getAllTags(@PathVariable("id") Long id) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from Tag as t inner join t.games as g where g.id = :id");
            query.setParameter("id", id);
            List<Tag> tags = query.list();
            session.getTransaction().commit();
            return new ResponseEntity<>(tags.stream().map((tag) -> modelMapper.map(tag, TagDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private GameDTO castToDTO(Game game) {
        GameDTO gameDTO = modelMapper.map(game, GameDTO.class);
        // convert tag objects into tag ids, so we pass less data
        gameDTO.setTagIds(game.getTags().stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        return gameDTO;
    }
}
