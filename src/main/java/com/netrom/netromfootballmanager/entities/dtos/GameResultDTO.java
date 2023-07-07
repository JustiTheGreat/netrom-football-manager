package com.netrom.netromfootballmanager.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GameResultDTO {

    private Long id;

    private Integer goalsTeamOne;

    private Integer goalsTeamTwo;

    private Long gameId;
}
