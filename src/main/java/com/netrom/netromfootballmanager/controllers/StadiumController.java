package com.netrom.netromfootballmanager.controllers;

import com.netrom.netromfootballmanager.mappers.Mapper;
import com.netrom.netromfootballmanager.entities.daos.StadiumDAO;
import com.netrom.netromfootballmanager.entities.dtos.StadiumDTO;
import com.netrom.netromfootballmanager.services.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stadium")
public class StadiumController {

    @Autowired
    private StadiumService stadiumService;

    @PostMapping
    public ResponseEntity<StadiumDTO> createStadium(@RequestBody StadiumDTO stadium) {
        if (stadium == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        StadiumDAO resultDB = stadiumService.create(Mapper.DTOToDAO(stadium));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        StadiumDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StadiumDTO> getStadiumById(@PathVariable("id") long stadiumId) {
        StadiumDAO resultDB = stadiumService.getById(stadiumId);
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        StadiumDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<StadiumDTO>> getStadiumList() {
        List<StadiumDAO> resultDB = stadiumService.getList();
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<StadiumDTO> result = resultDB.stream().map(Mapper::DAOToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StadiumDTO> updateStadium(@PathVariable("id") long stadiumId, @RequestBody StadiumDTO stadium) {
        StadiumDAO resultDB = stadiumService.update(stadiumId, Mapper.DTOToDAO(stadium));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        StadiumDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StadiumDTO> deleteStadiumById(@PathVariable("id") long stadiumId) {
        stadiumService.deleteById(stadiumId);
        if (stadiumService.getById(stadiumId) != null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
