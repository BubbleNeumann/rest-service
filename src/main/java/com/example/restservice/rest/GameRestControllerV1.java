//package com.example.restservice.rest;
//
//
//import com.example.restservice.model.Game;
//import com.example.restservice.service.GameService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/games")
//public class GameRestControllerV1 {
//    @Autowired
//    private GameService gameService;
//
//    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Game> getGame(@PathVariable("id") Long gameId) {
//        if (gameId == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        Game game = this.gameService.getById(gameId);
//        if (game == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(game, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Game> saveGame(@RequestBody Game game) {
//        HttpHeaders headers = new HttpHeaders();
//        if (game == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        this.gameService.save(game);
//        return new ResponseEntity<>(game, headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Game> updateGame(@RequestBody Game game, UriComponentsBuilder builder) {
//        HttpHeaders headers = new HttpHeaders();
//
//        if (game == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        this.gameService.save(game);
//        return new ResponseEntity<>(game, headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Game> deleteGame(Long id) {
//        Game game = this.gameService.getById(id);
//        if (game == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        this.gameService.delete(id);
//        return new ResponseEntity<>(game, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<Game>> getAll() {
//        List<Game> games = this.gameService.getAll();
//        if (games.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(games, HttpStatus.OK);
//    }
//}
