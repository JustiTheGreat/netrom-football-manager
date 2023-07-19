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

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

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
                playerService.create(player);
            }
        });
        teamDAO.getGamesAsTeamOne().forEach(game -> {
            if (game != null) {
                game.setTeamOne(null);
                gameService.create(game);
            }
        });
        teamDAO.getGamesAsTeamTwo().forEach(game -> {
            if (game != null) {
                game.setTeamTwo(null);
                gameService.create(game);
            }
        });
    }
}
