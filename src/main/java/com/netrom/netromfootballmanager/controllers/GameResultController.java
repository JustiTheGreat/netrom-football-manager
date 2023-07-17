package com.netrom.netromfootballmanager.controllers;

import com.netrom.netromfootballmanager.entities.daos.GameDAO;
import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;
import com.netrom.netromfootballmanager.entities.dtos.GameResultDTO;
import com.netrom.netromfootballmanager.entities.dtos.GameTeamsNamesDTO;
import com.netrom.netromfootballmanager.mappers.Mapper;
import com.netrom.netromfootballmanager.services.GameResultService;
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
@RequestMapping("/games-results")
public class GameResultController {

    @Autowired
    private GameResultService gameResultService;

    @Autowired
    private GameService gameService;

    @Autowired
    private Mapper mapper;

    @PostMapping
    public ResponseEntity<GameResultDTO> createGameResult(@RequestBody GameResultDTO requestDTO) {
        if (requestDTO == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        GameResultDAO resultDB = gameResultService.create(mapper.DTOToDAO(requestDTO));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        GameResultDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GameResultDTO>> getGameResultList() {
        List<GameResultDAO> resultDB = gameResultService.getList();
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        List<GameResultDTO> result = resultDB.stream().map(game -> mapper.DAOToDTO(game)).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResultDTO> getGameResultById(@PathVariable("id") long id) {
        GameResultDAO resultDB;
        try {
            resultDB = gameResultService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GameResultDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResultDTO> updateGameResult(@PathVariable("id") long id, @RequestBody GameResultDTO requestDTO) {
        if (id != requestDTO.getId()) return new ResponseEntity<>(HttpStatus.CONFLICT);
        try {
            gameResultService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GameResultDAO resultDB = gameResultService.update(id, mapper.DTOToDAO(requestDTO));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        GameResultDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GameResultDTO> deleteGameResultById(@PathVariable("id") long id) {
        try {
            gameResultService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        gameResultService.removeReferences(id);
        gameResultService.deleteById(id);
        try {
            gameResultService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @CrossOrigin
    @GetMapping("/game-teams-names/{id}")
    public ResponseEntity<GameTeamsNamesDTO> getGameTeamsNamesByGameResult(@PathVariable("id") long id) {
        GameResultDAO gameResultDAO = new GameResultDAO(id, null, null, null);
        GameDAO resultDB = gameService.findGameByGameResult(gameResultDAO);
        if (resultDB == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String teamOneName = resultDB.getTeamOne() == null ? null : resultDB.getTeamOne().getName();
        String teamTwoName = resultDB.getTeamTwo() == null ? null : resultDB.getTeamTwo().getName();
        GameTeamsNamesDTO result = new GameTeamsNamesDTO(teamOneName, teamTwoName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
