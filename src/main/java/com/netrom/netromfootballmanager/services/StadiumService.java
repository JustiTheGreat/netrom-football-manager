package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.StadiumDAO;

import java.util.List;

public interface StadiumService {

    StadiumDAO create(StadiumDAO stadium);

    StadiumDAO getById(long stadiumId);

    List<StadiumDAO> getList();

    StadiumDAO update(long stadiumId, StadiumDAO stadium);

    void deleteById(long stadiumId);
}
