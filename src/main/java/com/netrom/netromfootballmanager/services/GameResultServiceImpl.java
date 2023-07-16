package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.GameDAO;
import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;
import com.netrom.netromfootballmanager.repositories.GameRepository;
import com.netrom.netromfootballmanager.repositories.GameResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameResultServiceImpl implements GameResultService {
    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public GameResultDAO create(GameResultDAO dao) {
        return gameResultRepository.save(dao);
    }

    @Override
    public GameResultDAO getById(long id) {
        return gameResultRepository.getReferenceById(id);
    }

    @Override
    public List<GameResultDAO> getList() {
        return gameResultRepository.findAll();
    }

    @Override
    public GameResultDAO update(long id, GameResultDAO newDao) {
        GameResultDAO gameDAO = gameResultRepository.getReferenceById(id);
        if (newDao.getGoalsTeamOne() != null && newDao.getGoalsTeamOne() >= 0) {
            gameDAO.setGoalsTeamOne(newDao.getGoalsTeamOne());
        }
        if (newDao.getGoalsTeamTwo() != null) {
            gameDAO.setGoalsTeamTwo(newDao.getGoalsTeamTwo());
        }
        if (newDao.getGame() != null) {
            gameDAO.setGame(newDao.getGame());
        }
        return gameResultRepository.save(gameDAO);
    }

    @Override
    public void deleteById(long id) {
        gameResultRepository.deleteById(id);
    }

    @Override
    public void removeReferences(long id) {
        GameResultDAO gameResultDAO = gameResultRepository.getReferenceById(id);
        GameDAO game = gameResultDAO.getGame();
        if (game != null) {
            game.setGameResult(null);
            gameRepository.save(game);
        }
    }
}
