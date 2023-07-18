package com.netrom.netromfootballmanager.controllers;

import com.netrom.netromfootballmanager.mappers.Mapper;
import com.netrom.netromfootballmanager.entities.daos.GameDAO;
import com.netrom.netromfootballmanager.entities.dtos.GameDTO;
import com.netrom.netromfootballmanager.services.GameService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private Mapper mapper;

    @PostMapping
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO game) {
        if (game == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        GameDAO resultDB = gameService.create(mapper.DTOToDAO(game));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        GameDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable("id") long id) {
        GameDAO resultDB = gameService.getById(id);
        try {
            resultDB = gameService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GameDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GameDTO>> getGameList() {
        List<GameDAO> resultDB = gameService.getList();
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        List<GameDTO> result = resultDB.stream().map(game -> mapper.DAOToDTO(game)).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> updateGame(@PathVariable("id") long id, @RequestBody GameDTO requestDTO) {
        if (id != requestDTO.getId()) return new ResponseEntity<>(HttpStatus.CONFLICT);
        try {
            gameService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GameDAO resultDB = gameService.update(id, mapper.DTOToDAO(requestDTO));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        GameDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GameDTO> deleteGameById(@PathVariable("id") long id) {
        try {
            gameService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        gameService.deleteById(id);
        try {
            gameService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("random-game-result/{id}")
    public ResponseEntity<GameDTO> generateRandomGameResult(@PathVariable("id") long id) {
        try {
            gameService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GameDAO resultDB = gameService.generateRandomGameResult(id);
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        GameDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}