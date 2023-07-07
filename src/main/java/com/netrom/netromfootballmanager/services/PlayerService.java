package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.PlayerDAO;

import java.util.List;

public interface PlayerService {

    PlayerDAO create(PlayerDAO player);

    PlayerDAO getById(long playerId);

    List<PlayerDAO> getList();

    PlayerDAO update(long playerId, PlayerDAO player);

    void deleteById(long playerId);
}
