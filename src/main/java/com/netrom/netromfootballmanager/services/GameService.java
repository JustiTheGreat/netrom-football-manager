package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.GameDAO;

import java.util.List;

public interface GameService {

    GameDAO create(GameDAO game);

    GameDAO getById(long gameId);

    List<GameDAO> getList();

    GameDAO update(long gameId, GameDAO game);

    void deleteById(long gameId);
}
