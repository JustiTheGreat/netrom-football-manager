package com.netrom.netromfootballmanager.controllers;

import com.netrom.netromfootballmanager.mappers.Mapper;
import com.netrom.netromfootballmanager.entities.daos.TeamDAO;
import com.netrom.netromfootballmanager.entities.dtos.TeamDTO;
import com.netrom.netromfootballmanager.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO team) {
        if (team == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        TeamDAO resultDB = teamService.create(Mapper.DTOToDAO(team));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        TeamDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable("id") long teamId) {
        TeamDAO resultDB = teamService.getById(teamId);
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        TeamDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TeamDTO>> getTeamList() {
        List<TeamDAO> resultDB = teamService.getList();
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<TeamDTO> result = resultDB.stream().map(Mapper::DAOToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable("id") long teamId, @RequestBody TeamDTO team) {
        TeamDAO resultDB = teamService.update(teamId, Mapper.DTOToDAO(team));
        if (resultDB == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        TeamDTO result = Mapper.DAOToDTO(resultDB);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TeamDTO> deleteTeamById(@PathVariable("id") long teamId) {
        teamService.deleteById(teamId);
        if (teamService.getById(teamId) != null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
