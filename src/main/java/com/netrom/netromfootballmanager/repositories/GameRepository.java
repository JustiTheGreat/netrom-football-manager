package com.netrom.netromfootballmanager.repositories;

import com.netrom.netromfootballmanager.entities.daos.GameDAO;
import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameDAO, Long> {
    GameDAO findFirstByGameResult(GameResultDAO gameResult);
}
