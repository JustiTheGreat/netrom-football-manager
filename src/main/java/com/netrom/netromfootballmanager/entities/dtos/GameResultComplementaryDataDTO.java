package com.netrom.netromfootballmanager.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GameResultComplementaryDataDTO {

    private Long dateAndTimeInMillis;

    private String teamOneName;

    private String teamTwoName;
}
