package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.PlayerDAO;
import com.netrom.netromfootballmanager.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public PlayerDAO create(PlayerDAO player) {
        return playerRepository.save(player);
    }

    @Override
    public PlayerDAO getById(long playerId) {
        return playerRepository.getReferenceById(playerId);
    }

    @Override
    public List<PlayerDAO> getList() {
        return playerRepository.findAll();
    }

    @Override
    public PlayerDAO update(long playerId, PlayerDAO player) {
        PlayerDAO playerDB = playerRepository.getReferenceById(playerId);
        if (player.getName() != null && !player.getName().isBlank()) {
            playerDB.setName(player.getName());
        }
        if (player.getGoalsScored() != null && player.getGoalsScored() >= 0) {
            playerDB.setGoalsScored(player.getGoalsScored());
        }
        if (player.getRole() != null) {
            playerDB.setRole(player.getRole());
        }
        playerDB.setTeam(player.getTeam());
        return playerRepository.save(playerDB);
    }

    @Override
    public void deleteById(long playerId) {
        playerRepository.deleteById(playerId);
    }
}
