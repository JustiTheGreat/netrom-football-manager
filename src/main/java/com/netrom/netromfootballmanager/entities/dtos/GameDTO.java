package com.netrom.netromfootballmanager.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GameDTO {

    private Long id;

    private Long dateAndTimeInMillis;

    private Long teamOneId;

    private Long teamTwoId;

    private Long stadiumId;

    private Long gameResultId;
}
