package com.example.restservice.rest;

import com.example.restservice.dto.GameDTO;
import com.example.restservice.dto.TagDTO;
import com.example.restservice.model.Game;
import com.example.restservice.model.Tag;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/tags")
public class TagController {
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
    public ResponseEntity<TagDTO> deleteGame(@PathVariable("id") Long id) {
        Tag tag = this.tagService.getById(id);
        if (tag == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // TODO remove corresponding junction table entries
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
     * @param id - tag id.
     * @return all games marked with the tag with given id.
     */
    @RequestMapping(value = "{id}/games", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDTO>> getAllGames(@PathVariable("id") Long id) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from Game as g inner join g.tags as t where t.id = :id");
            query.setParameter("id", id);
            List<Game> games = query.list();
            session.getTransaction().commit();
            return new ResponseEntity<>(games.stream().map((game) -> modelMapper.map(game, GameDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}