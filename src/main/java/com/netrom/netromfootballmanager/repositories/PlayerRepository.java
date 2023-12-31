package com.netrom.netromfootballmanager.repositories;

import com.netrom.netromfootballmanager.entities.daos.PlayerDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerDAO, Long> {
}
