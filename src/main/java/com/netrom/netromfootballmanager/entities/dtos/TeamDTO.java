package com.netrom.netromfootballmanager.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class TeamDTO {

    private Long id;

    private String name;

    private List<Long> playersIds;

    private List<Long> gamesAsTeamOneIds;

    private List<Long> gamesAsTeamTwoIds;

    private Integer goalsScored;

    private Integer goalsReceived;

    private Integer victories;

    private Integer defeats;

    private Integer draws;
}
