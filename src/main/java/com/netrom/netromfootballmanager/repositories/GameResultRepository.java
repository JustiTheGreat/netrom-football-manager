package com.netrom.netromfootballmanager.repositories;

import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResultDAO, Long> {
}
