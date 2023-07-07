package com.netrom.netromfootballmanager.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int goalsScored;

    @Column(nullable = false)
    private int goalsReceived;

    @Column(nullable = false)
    private int victories;

    @Column(nullable = false)
    private int defeats;

    @Column(nullable = false)
    private int draws;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Player> players;

    @OneToMany(mappedBy = "teamOne", fetch = FetchType.LAZY)
    private List<Game> gamesAsTeamOne;

    @OneToMany(mappedBy = "teamTwo", fetch = FetchType.LAZY)
    private List<Game> gamesAsTeamTwo;
}
