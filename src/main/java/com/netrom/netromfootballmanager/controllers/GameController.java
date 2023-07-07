package com.netrom.netromfootballmanager.controllers;

import com.netrom.netromfootballmanager.mappers.Mapper;
import com.netrom.netromfootballmanager.entities.daos.GameDAO;
import com.netrom.netromfootballmanager.entities.dtos.GameDTO;
import com.netrom.netromfootballmanager.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO game) {
        if (game == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        GameDAO resultDB = gameService.create(Mapper.DTOToDAO(game));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        GameDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable("id") long gameId) {
        GameDAO resultDB = gameService.getById(gameId);
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        GameDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GameDTO>> getGameList() {
        List<GameDAO> resultDB = gameService.getList();
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<GameDTO> result = resultDB.stream().map(Mapper::DAOToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> updateGame(@PathVariable("id") long gameId, @RequestBody GameDTO game) {
        GameDAO resultDB = gameService.update(gameId, Mapper.DTOToDAO(game));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        GameDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GameDTO> deleteGameById(@PathVariable("id") long gameId) {
        gameService.deleteById(gameId);
        if (gameService.getById(gameId) != null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}