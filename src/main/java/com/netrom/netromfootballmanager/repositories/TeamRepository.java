package com.netrom.netromfootballmanager.repositories;

import com.netrom.netromfootballmanager.entities.daos.TeamDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamDAO, Long> {
}
