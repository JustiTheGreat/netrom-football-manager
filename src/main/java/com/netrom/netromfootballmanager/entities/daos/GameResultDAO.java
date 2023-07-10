package com.netrom.netromfootballmanager.entities.daos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResultDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column
    private Integer goalsTeamOne;

    @Column
    private Integer goalsTeamTwo;

    @OneToOne(mappedBy = "gameResult", fetch = FetchType.LAZY)
    private GameDAO game;

    public Long getGameId() {
        if (game == null) return null;
        return game.getId();
    }
}
