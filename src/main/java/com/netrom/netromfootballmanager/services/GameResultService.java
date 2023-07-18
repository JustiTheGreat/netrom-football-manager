package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;

import java.util.List;

public interface GameResultService {

    GameResultDAO create(GameResultDAO dao);

    GameResultDAO getById(long id);

    List<GameResultDAO> getList();

    GameResultDAO update(long id, GameResultDAO newDao);

    void deleteById(long id);

    void removeReferences(long id);
}
