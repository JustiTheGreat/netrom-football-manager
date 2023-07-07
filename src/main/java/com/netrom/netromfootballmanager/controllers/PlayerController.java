package com.netrom.netromfootballmanager.controllers;

import com.netrom.netromfootballmanager.mappers.Mapper;
import com.netrom.netromfootballmanager.entities.daos.PlayerDAO;
import com.netrom.netromfootballmanager.entities.dtos.PlayerDTO;
import com.netrom.netromfootballmanager.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO player) {
        if (player == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        PlayerDAO resultDB = playerService.create(Mapper.DTOToDAO(player));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        PlayerDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable("id") long playerId) {
        PlayerDAO resultDB = playerService.getById(playerId);
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        PlayerDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getPlayerList() {
        List<PlayerDAO> resultDB = playerService.getList();
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<PlayerDTO> result = resultDB.stream().map(Mapper::DAOToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable("id") long playerId, @RequestBody PlayerDTO player) {
        PlayerDAO resultDB = playerService.update(playerId, Mapper.DTOToDAO(player));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        PlayerDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlayerDTO> deletePlayerById(@PathVariable("id") long playerId) {
        playerService.deleteById(playerId);
        if (playerService.getById(playerId) != null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
