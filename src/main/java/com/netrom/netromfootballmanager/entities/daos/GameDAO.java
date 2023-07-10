package com.netrom.netromfootballmanager.entities.daos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column
    private Long dateAndTimeInMillis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_one_id")
    private TeamDAO teamOne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_two_id")
    private TeamDAO teamTwo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private StadiumDAO stadium;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private GameResultDAO gameResult;

    public Long getTeamOneId() {
        if (teamOne == null) return null;
        return teamOne.getId();
    }

    public Long getTeamTwoId() {
        if (teamTwo == null) return null;
        return teamTwo.getId();
    }

    public Long getStadiumId() {
        if (stadium == null) return null;
        return stadium.getId();
    }

    public Long getGameResultId() {
        if (gameResult == null) return null;
        return gameResult.getId();
    }
}
