package com.example.restservice.rest;

import com.example.restservice.dto.GameDTO;
import com.example.restservice.dto.TagDTO;
import com.example.restservice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @RequestMapping(value = "id={id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> getTag(@PathVariable("id") Long tagId) {
        if (tagId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TagDTO tagDTO = this.tagService.getById(tagId);
        if (tagDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tagDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> saveTag(@RequestBody TagDTO tagDTO) {
        if (tagDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        tagDTO.setId(this.tagService.save(tagDTO));
        return new ResponseEntity<>(tagDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "id={id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> deleteTag(@PathVariable("id") Long id) {
        TagDTO tagDTO = this.tagService.getById(id);
        if (tagDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.tagService.delete(id);
        return new ResponseEntity<>(tagDTO, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"", "page={page}", "size={size}", "page={page}/size={size}"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<TagDTO>> getAll(
            @PathVariable(value = "page", required = false) Integer pageNum,
            @PathVariable(value = "size", required = false) Integer size
    ) {
        List<TagDTO> tagDTOs = this.tagService.getAll(
                pageNum == null ? 1 : pageNum,
                size == null ? 1 : size
        );
        if (tagDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
    }

    /**
     * Get all games with the tag.
     *
     * @param id - tag id.
     * @return all games marked with the tag with given id.
     */
    @RequestMapping(value = "id={id}/games", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDTO>> getAllGames(@PathVariable("id") Long id) {
        try {
            List<GameDTO> gameDTOs = this.tagService.getAllGames(id);
            if (gameDTOs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(gameDTOs, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
