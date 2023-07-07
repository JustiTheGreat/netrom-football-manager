package com.netrom.netromfootballmanager.controllers;

import com.netrom.netromfootballmanager.mappers.Mapper;
import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;
import com.netrom.netromfootballmanager.entities.dtos.GameResultDTO;
import com.netrom.netromfootballmanager.services.GameResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game-result")
public class GameResultController {

    @Autowired
    private GameResultService gameResultService;

    @PostMapping
    public ResponseEntity<GameResultDTO> createGameResult(@RequestBody GameResultDTO gameResult) {
        if (gameResult == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        GameResultDAO resultDB = gameResultService.create(Mapper.DTOToDAO(gameResult));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        GameResultDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResultDTO> getGameResultById(@PathVariable("id") long gameResultId) {
        GameResultDAO resultDB = gameResultService.getById(gameResultId);
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        GameResultDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GameResultDTO>> getGameResultList() {
        List<GameResultDAO> resultDB = gameResultService.getList();
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<GameResultDTO> result = resultDB.stream().map(Mapper::DAOToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResultDTO> updateGameResult(@PathVariable("id") long gameResultId, @RequestBody GameResultDTO gameResult) {
        GameResultDAO resultDB = gameResultService.update(gameResultId, Mapper.DTOToDAO(gameResult));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        GameResultDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GameResultDTO> deleteGameResultById(@PathVariable("id") long gameResultId) {
        gameResultService.deleteById(gameResultId);
        if (gameResultService.getById(gameResultId) != null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
