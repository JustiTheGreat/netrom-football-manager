package com.netrom.netromfootballmanager.repositories;

import com.netrom.netromfootballmanager.entities.daos.GameDAO;
import com.netrom.netromfootballmanager.entities.daos.TeamDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<GameDAO, Long> {

    List<GameDAO> findAllByTeamOne(TeamDAO team);

    List<GameDAO> findAllByTeamTwo(TeamDAO team);
}
