package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.GameDAO;
import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;
import com.netrom.netromfootballmanager.entities.daos.TeamDAO;

import java.util.List;

public interface GameService {

    GameDAO create(GameDAO game);

    GameDAO getById(long gameId);

    List<GameDAO> getList();

    GameDAO update(long gameId, GameDAO game);

    void deleteById(long gameId);

    GameDAO findGameByGameResult(GameResultDAO gameResult);

    List<GameDAO> findAllByTeamOne(TeamDAO team);

    List<GameDAO> findAllByTeamTwo(TeamDAO team);
}
