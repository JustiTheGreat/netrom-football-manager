package com.netrom.netromfootballmanager.repositories;

import com.netrom.netromfootballmanager.entities.daos.GameResultDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<GameResultDAO, Long> {
}
