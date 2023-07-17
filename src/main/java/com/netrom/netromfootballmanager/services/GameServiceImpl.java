package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.GameDAO;
import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;
import com.netrom.netromfootballmanager.entities.daos.TeamDAO;
import com.netrom.netromfootballmanager.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public GameDAO create(GameDAO game) {
        return gameRepository.save(game);
    }

    @Override
    public GameDAO getById(long gameId) {
        return gameRepository.getReferenceById(gameId);
    }

    @Override
    public List<GameDAO> getList() {
        return gameRepository.findAll();
    }

    @Override
    public GameDAO update(long gameId, GameDAO game) {
        GameDAO gameDB = gameRepository.getReferenceById(gameId);
        if (game.getDateAndTimeInMillis() != null) {
            gameDB.setDateAndTimeInMillis(game.getDateAndTimeInMillis());
        }
        if (game.getTeamOne() != null) {
            gameDB.setTeamOne(game.getTeamOne());
        }
        if (game.getTeamTwo() != null) {
            gameDB.setTeamTwo(game.getTeamTwo());
        }
        if (game.getStadium() != null) {
            gameDB.setStadium(game.getStadium());
        }
        if (game.getGameResult() != null) {
            gameDB.setGameResult(game.getGameResult());
        }
        return gameRepository.save(gameDB);
    }

    @Override
    public void deleteById(long gameId) {
        gameRepository.deleteById(gameId);
    }

    public GameDAO findGameByGameResult(GameResultDAO gameResult) {
        return gameRepository.findFirstByGameResult(gameResult);
    }

    @Override
    public List<GameDAO> findAllByTeamOne(TeamDAO team) {
        return gameRepository.findAllByTeamOne(team);
    }

    @Override
    public List<GameDAO> findAllByTeamTwo(TeamDAO team) {
        return gameRepository.findAllByTeamTwo(team);
    }
}
