package com.netrom.netromfootballmanager.controllers;

import com.netrom.netromfootballmanager.entities.daos.StadiumDAO;
import com.netrom.netromfootballmanager.entities.dtos.StadiumDTO;
import com.netrom.netromfootballmanager.mappers.Mapper;
import com.netrom.netromfootballmanager.services.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stadiums")
public class StadiumController {

    @Autowired
    private StadiumService stadiumService;

    @Autowired
    private Mapper mapper;

    @PostMapping
    public ResponseEntity<StadiumDTO> createStadium(@RequestBody StadiumDTO requestDTO) {
        if (requestDTO == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        StadiumDAO resultDB = stadiumService.create(mapper.DTOToDAO(requestDTO));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        StadiumDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StadiumDTO>> getStadiumList() {
        List<StadiumDAO> resultDB = stadiumService.getList();
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        List<StadiumDTO> result = resultDB.stream().map(game -> mapper.DAOToDTO(game)).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StadiumDTO> getStadiumById(@PathVariable("id") long id) {
        StadiumDAO resultDB = stadiumService.getById(id);
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        StadiumDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StadiumDTO> updateStadium(@PathVariable("id") long id, @RequestBody StadiumDTO requestDTO) {
        if (id != requestDTO.getId()) return new ResponseEntity<>(HttpStatus.CONFLICT);
        if (stadiumService.getById(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        StadiumDAO resultDB = stadiumService.update(id, mapper.DTOToDAO(requestDTO));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        StadiumDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StadiumDTO> deleteStadiumById(@PathVariable("id") long id) {
        if (stadiumService.getById(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        stadiumService.deleteById(id);
        if (stadiumService.getById(id) != null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
