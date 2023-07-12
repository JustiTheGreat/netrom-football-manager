package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.TeamDAO;

import java.util.List;

public interface TeamService {

    TeamDAO create(TeamDAO dao);

    TeamDAO getById(long id);

    List<TeamDAO> getList();

    TeamDAO update(long id, TeamDAO newDao);

    void deleteById(long id);

    void removeReferences(long id);
}
