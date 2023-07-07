package com.netrom.netromfootballmanager.entities.dtos;

import com.netrom.netromfootballmanager.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PlayerDTO {

    private Long id;

    private String name;

    private Integer goalsScored;

    private Role role;

    private Long teamId;
}
