package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;

import java.util.List;

public interface GameResultService {

    GameResultDAO create(GameResultDAO result);

    GameResultDAO getById(long resultId);

    List<GameResultDAO> getList();

    GameResultDAO update(long resultId, GameResultDAO result);

    void deleteById(long resultId);
}
