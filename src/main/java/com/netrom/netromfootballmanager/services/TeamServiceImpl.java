package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.TeamDAO;
import com.netrom.netromfootballmanager.repositories.GameRepository;
import com.netrom.netromfootballmanager.repositories.PlayerRepository;
import com.netrom.netromfootballmanager.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public TeamDAO create(TeamDAO dao) {
        return teamRepository.save(dao);
    }

    @Override
    public TeamDAO getById(long id) {
        return teamRepository.getReferenceById(id);
    }

    @Override
    public List<TeamDAO> getList() {
        return teamRepository.findAll();
    }

    @Override
    public TeamDAO update(long id, TeamDAO newDao) {
        TeamDAO teamDB = teamRepository.getReferenceById(id);
        if (newDao.getName() != null && !newDao.getName().isBlank()) {
            teamDB.setName(newDao.getName());
        }
        if (newDao.getGoalsScored() != null && newDao.getGoalsScored() >= 0) {
            teamDB.setGoalsScored(newDao.getGoalsScored());
        }
        if (newDao.getGoalsReceived() != null && newDao.getGoalsReceived() >= 0) {
            teamDB.setGoalsReceived(newDao.getGoalsReceived());
        }
        if (newDao.getVictories() != null && newDao.getVictories() >= 0) {
            teamDB.setVictories(newDao.getVictories());
        }
        if (newDao.getDefeats() != null && newDao.getDefeats() >= 0) {
            teamDB.setDefeats(newDao.getDefeats());
        }
        if (newDao.getDraws() != null && newDao.getDraws() >= 0) {
            teamDB.setDraws(newDao.getDraws());
        }
        if (newDao.getPlayers() != null) {
            teamDB.setPlayers(newDao.getPlayers());
        }
        if (newDao.getGamesAsTeamOne() != null) {
            teamDB.setGamesAsTeamOne(newDao.getGamesAsTeamOne());
        }
        if (newDao.getGamesAsTeamTwo() != null) {
            teamDB.setGamesAsTeamTwo(newDao.getGamesAsTeamTwo());
        }
        return teamRepository.save(teamDB);
    }

    @Override
    public void deleteById(long id) {
        teamRepository.deleteById(id);
    }

    @Override
    public void removeReferences(long id) {
        TeamDAO teamDAO = teamRepository.getReferenceById(id);
        teamDAO.getPlayers().forEach(player -> {
            if (player != null) {
                player.setTeam(null);
                playerRepository.save(player);
            }
        });
        teamDAO.getGamesAsTeamOne().forEach(game -> {
            if (game != null) {
                game.setTeamOne(null);
                gameRepository.save(game);
            }
        });
        teamDAO.getGamesAsTeamTwo().forEach(game -> {
            if (game != null) {
                game.setTeamTwo(null);
                gameRepository.save(game);
            }
        });
    }
}
