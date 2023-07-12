package com.netrom.netromfootballmanager.controllers;

import com.netrom.netromfootballmanager.entities.daos.PlayerDAO;
import com.netrom.netromfootballmanager.entities.dtos.PlayerDTO;
import com.netrom.netromfootballmanager.mappers.Mapper;
import com.netrom.netromfootballmanager.services.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private Mapper mapper;

    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO requestDTO) {
        if (requestDTO == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        PlayerDAO resultDB = playerService.create(mapper.DTOToDAO(requestDTO));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        PlayerDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getPlayerList() {
        List<PlayerDAO> resultDB = playerService.getList();
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        List<PlayerDTO> result = resultDB.stream().map(game -> mapper.DAOToDTO(game)).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable("id") long id) {
        PlayerDAO resultDB;
        try {
            resultDB = playerService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        PlayerDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable("id") long id, @RequestBody PlayerDTO requestDTO) {
        if (id != requestDTO.getId()) return new ResponseEntity<>(HttpStatus.CONFLICT);
        try {
            playerService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        PlayerDAO resultDB = playerService.update(id, mapper.DTOToDAO(requestDTO));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        PlayerDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlayerDTO> deletePlayerById(@PathVariable("id") long id) {
        try {
            playerService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        playerService.deleteById(id);
        try {
            playerService.getById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
