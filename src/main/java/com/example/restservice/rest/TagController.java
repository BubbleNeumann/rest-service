package com.example.restservice.rest;

import com.example.restservice.dto.GameDTO;
import com.example.restservice.dto.TagDTO;
import com.example.restservice.model.Game;
import com.example.restservice.model.Tag;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/tags")
public class TagController {
    @Autowired
    private GameService gameService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ModelMapper modelMapper;


    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> getTag(@PathVariable("id") Long tagId) {
        if (tagId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Tag tag = this.tagService.getById(tagId);
        if (tag == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(modelMapper.map(tag, TagDTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> saveTag(@RequestBody TagDTO tagDTO) {
        if (tagDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        this.tagService.save(modelMapper.map(tagDTO, Tag.class));
        return new ResponseEntity<>(tagDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> deleteTag(@PathVariable("id") Long id) {
        Tag tag = this.tagService.getById(id);
        if (tag == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // for all games which have this tag bound update their tags set
        List<Game> games = getAllGamesWithTag(id);
        System.out.println(games);
        if (!games.isEmpty()) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Set<Tag> tags;
            for (Game game : games) {
                try (Session session = sf.openSession()) {
                    session.beginTransaction();
                    Query query = session.createQuery("from Tag as t inner join t.games as g where g.id = :id");
                    query.setParameter("id", game.getId());
                    tags = new HashSet<Tag>(query.list());
                    session.getTransaction().commit();
                } catch (Exception e) {
                    StandardServiceRegistryBuilder.destroy(registry);
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                tags.remove(tag);
                game.setTags(tags);
                this.gameService.save(game);
            }
        }
        this.tagService.delete(id);
        return new ResponseEntity<>(modelMapper.map(tag, TagDTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagDTO>> getAll() {
        List<Tag> tags = this.tagService.getAll();
        if (tags.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tags.stream().map((tag) -> modelMapper.map(tag, TagDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get all games with the tag.
     *
     * @param id - tag id.
     * @return all games marked with the tag with given id.
     */
    @RequestMapping(value = "{id}/games", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDTO>> getAllGames(@PathVariable("id") Long id) {
        List<Game> games = getAllGamesWithTag(id);
        if (games.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(games.stream().map((game) -> modelMapper.map(game, GameDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * @param tagId
     * @return list of games with the tag if any were found otherwise return empty ArrayList.
     */
    private List<Game> getAllGamesWithTag(Long tagId) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from Game as g inner join g.tags as t where t.id = :id");
            query.setParameter("id", tagId);
            List<Game> games = query.list();
            session.getTransaction().commit();
            return games;
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            return new ArrayList<Game>();
        }
    }
}
