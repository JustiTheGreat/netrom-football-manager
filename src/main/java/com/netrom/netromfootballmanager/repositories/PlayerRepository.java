package com.netrom.netromfootballmanager.repositories;

import com.netrom.netromfootballmanager.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlayerRepository extends JpaRepository<Player, Long> {
}
