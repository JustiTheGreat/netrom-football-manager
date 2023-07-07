package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.TeamDAO;

import java.util.List;

public interface TeamService {

    TeamDAO create(TeamDAO team);

    TeamDAO getById(long teamId);

    List<TeamDAO> getList();

    TeamDAO update(long teamId, TeamDAO team);

    void deleteById(long teamId);
}
