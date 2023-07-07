package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;
import com.netrom.netromfootballmanager.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameResultServiceImpl implements GameResultService {
    @Autowired
    private ResultRepository resultRepository;

    @Override
    public GameResultDAO create(GameResultDAO result) {
        return resultRepository.save(result);
    }

    @Override
    public GameResultDAO getById(long resultId) {
        return resultRepository.getReferenceById(resultId);
    }

    @Override
    public List<GameResultDAO> getList() {
        return resultRepository.findAll();
    }

    @Override
    public GameResultDAO update(long resultId, GameResultDAO result) {
        GameResultDAO gameDB = resultRepository.getReferenceById(resultId);
        if (result.getGoalsTeamOne() != null && result.getGoalsTeamOne() >= 0) {
            gameDB.setGoalsTeamOne(result.getGoalsTeamOne());
        }
        if (result.getGoalsTeamTwo() != null) {
            gameDB.setGoalsTeamTwo(result.getGoalsTeamTwo());
        }
        if (result.getGame() != null) {
            gameDB.setGame(result.getGame());
        }
        return resultRepository.save(gameDB);
    }

    @Override
    public void deleteById(long playerId) {
        resultRepository.deleteById(playerId);
    }
}
