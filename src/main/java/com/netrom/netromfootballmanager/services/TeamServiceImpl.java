package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.TeamDAO;
import com.netrom.netromfootballmanager.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public TeamDAO create(TeamDAO team) {
        return teamRepository.save(team);
    }

    @Override
    public TeamDAO getById(long teamId) {
        return teamRepository.getReferenceById(teamId);
    }

    @Override
    public List<TeamDAO> getList() {
        return teamRepository.findAll();
    }

    @Override
    public TeamDAO update(long teamId, TeamDAO team) {
        TeamDAO teamDB = teamRepository.getReferenceById(teamId);
        if (team.getName() != null && !team.getName().isBlank()) {
            teamDB.setName(team.getName());
        }
        if (team.getGoalsScored() != null && team.getGoalsScored() >= 0) {
            teamDB.setGoalsScored(team.getGoalsScored());
        }
        if (team.getGoalsReceived() != null && team.getGoalsReceived() >= 0) {
            teamDB.setGoalsReceived(team.getGoalsReceived());
        }
        if (team.getVictories() != null && team.getVictories() >= 0) {
            teamDB.setVictories(team.getVictories());
        }
        if (team.getDefeats() != null && team.getDefeats() >= 0) {
            teamDB.setDefeats(team.getDefeats());
        }
        if (team.getDraws() != null && team.getDraws() >= 0) {
            teamDB.setDraws(team.getDraws());
        }
        if (team.getPlayers() != null) {
            teamDB.setPlayers(team.getPlayers());
        }
        if (team.getGamesAsTeamOne() != null) {
            teamDB.setGamesAsTeamOne(team.getGamesAsTeamOne());
        }
        if (team.getGamesAsTeamTwo() != null) {
            teamDB.setGamesAsTeamTwo(team.getGamesAsTeamTwo());
        }
        return teamRepository.save(teamDB);
    }

    @Override
    public void deleteById(long teamId) {
        teamRepository.deleteById(teamId);
    }
}
