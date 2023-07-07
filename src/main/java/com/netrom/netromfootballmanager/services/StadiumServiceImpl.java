package com.netrom.netromfootballmanager.services;

import com.netrom.netromfootballmanager.entities.daos.StadiumDAO;
import com.netrom.netromfootballmanager.repositories.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StadiumServiceImpl implements StadiumService {

    @Autowired
    private StadiumRepository stadiumRepository;

    @Override
    public StadiumDAO create(StadiumDAO stadium) {
        return stadiumRepository.save(stadium);
    }

    @Override
    public StadiumDAO getById(long stadiumId) {
        return stadiumRepository.getReferenceById(stadiumId);
    }

    @Override
    public List<StadiumDAO> getList() {
        return stadiumRepository.findAll();
    }

    @Override
    public StadiumDAO update(long stadiumId, StadiumDAO stadium) {
        StadiumDAO stadiumDB = stadiumRepository.getReferenceById(stadiumId);
        if (stadium.getName() != null && !stadium.getName().isBlank()) {
            stadiumDB.setName(stadium.getName());
        }
        if (stadium.getLocation() != null && !stadium.getLocation().isBlank()) {
            stadiumDB.setLocation(stadium.getLocation());
        }
        if (stadium.getGames() != null) {
            stadiumDB.setGames(stadium.getGames());
        }
        return stadiumRepository.save(stadiumDB);
    }

    @Override
    public void deleteById(long stadiumId) {
        stadiumRepository.deleteById(stadiumId);
    }
}
