package com.netrom.netromfootballmanager.repositories;

import com.netrom.netromfootballmanager.entities.daos.GameDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameDAO, Long> {
}
