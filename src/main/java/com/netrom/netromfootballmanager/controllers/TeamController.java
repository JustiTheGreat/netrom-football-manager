package com.netrom.netromfootballmanager.controllers;

import com.netrom.netromfootballmanager.entities.daos.TeamDAO;
import com.netrom.netromfootballmanager.entities.dtos.TeamDTO;
import com.netrom.netromfootballmanager.mappers.Mapper;
import com.netrom.netromfootballmanager.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private Mapper mapper;

    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO requestDTO) {
        if (requestDTO == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        TeamDAO resultDB = teamService.create(mapper.DTOToDAO(requestDTO));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        TeamDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TeamDTO>> getTeamList() {
        List<TeamDAO> resultDB = teamService.getList();
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        List<TeamDTO> result = resultDB.stream().map(game -> mapper.DAOToDTO(game)).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable("id") long id) {
        TeamDAO resultDB = teamService.getById(id);
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        TeamDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable("id") long id, @RequestBody TeamDTO requestDTO) {
        if (id != requestDTO.getId()) return new ResponseEntity<>(HttpStatus.CONFLICT);
        if (teamService.getById(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        TeamDAO resultDB = teamService.update(id, mapper.DTOToDAO(requestDTO));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        TeamDTO result = mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TeamDTO> deleteTeamById(@PathVariable("id") long id) {
        if (teamService.getById(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        teamService.deleteById(id);
        if (teamService.getById(id) != null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
