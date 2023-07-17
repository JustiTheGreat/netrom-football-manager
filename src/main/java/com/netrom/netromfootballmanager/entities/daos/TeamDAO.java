package com.netrom.netromfootballmanager.entities.daos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<PlayerDAO> players;

    @OneToMany(mappedBy = "teamOne", fetch = FetchType.LAZY)
    private List<GameDAO> gamesAsTeamOne;

    @OneToMany(mappedBy = "teamTwo", fetch = FetchType.LAZY)
    private List<GameDAO> gamesAsTeamTwo;

    public List<Long> getPlayersIds() {
        if (players == null) return null;
        return players.stream().map(PlayerDAO::getId).collect(Collectors.toList());
    }

    public List<Long> getGamesAsTeamOneIds() {
        if (gamesAsTeamOne == null) return null;
        return gamesAsTeamOne.stream().map(GameDAO::getId).collect(Collectors.toList());
    }

    public List<Long> getGamesAsTeamTwoIds() {
        if (gamesAsTeamTwo == null) return null;
        return gamesAsTeamTwo.stream().map(GameDAO::getId).collect(Collectors.toList());
    }
}
