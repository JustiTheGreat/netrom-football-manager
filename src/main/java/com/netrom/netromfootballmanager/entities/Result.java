package com.netrom.netromfootballmanager.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false)
    private String goalsTeamOne;

    @Column(nullable = false)
    private String goalsTeamTwo;

    @OneToOne(mappedBy = "result", fetch = FetchType.LAZY)
    private Game game;
}
