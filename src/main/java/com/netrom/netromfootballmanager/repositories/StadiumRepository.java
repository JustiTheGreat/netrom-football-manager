package com.netrom.netromfootballmanager.repositories;

import com.netrom.netromfootballmanager.entities.daos.StadiumDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StadiumRepository extends JpaRepository<StadiumDAO, Long> {
}
