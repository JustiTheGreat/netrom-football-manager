package com.netrom.netromfootballmanager.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false)
    private long dateAndTimeInMillis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_one_id", nullable = false)
    private Team teamOne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_two_id", nullable = false)
    private Team teamTwo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id", nullable = false)
    private Result result;
}
