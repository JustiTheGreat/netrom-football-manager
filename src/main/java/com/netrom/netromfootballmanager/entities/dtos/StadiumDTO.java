package com.netrom.netromfootballmanager.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class StadiumDTO {

    private Long id;

    private String name;

    private String location;

    private List<Long> gamesIds;
}
