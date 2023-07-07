package com.netrom.netromfootballmanager.entities.daos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class GameResultDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Integer goalsTeamOne;

    @Column(nullable = false)
    private Integer goalsTeamTwo;

    @OneToOne(mappedBy = "gameResult", fetch = FetchType.LAZY)
    private GameDAO game;

    public Long getGameId() {
        if (game == null) return null;
        return game.getId();
    }
}
