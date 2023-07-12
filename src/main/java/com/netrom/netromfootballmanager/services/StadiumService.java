package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.StadiumDAO;

import java.util.List;

public interface StadiumService {

    StadiumDAO create(StadiumDAO dao);

    StadiumDAO getById(long id);

    List<StadiumDAO> getList();

    StadiumDAO update(long id, StadiumDAO newDao);

    void deleteById(long id);

    void removeReferences(long id);
}
