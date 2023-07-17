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

    @Autowired
    private GameService gameService;

    @Override
    public StadiumDAO create(StadiumDAO dao) {
        return stadiumRepository.save(dao);
    }

    @Override
    public StadiumDAO getById(long id) {
        return stadiumRepository.getReferenceById(id);
    }

    @Override
    public List<StadiumDAO> getList() {
        return stadiumRepository.findAll();
    }

    @Override
    public StadiumDAO update(long id, StadiumDAO newDao) {
        StadiumDAO stadiumDB = stadiumRepository.getReferenceById(id);
        if (newDao.getName() != null && !newDao.getName().isBlank()) {
            stadiumDB.setName(newDao.getName());
        }
        if (newDao.getLocation() != null && !newDao.getLocation().isBlank()) {
            stadiumDB.setLocation(newDao.getLocation());
        }
        if (newDao.getGames() != null) {
            stadiumDB.setGames(newDao.getGames());
        }
        return stadiumRepository.save(stadiumDB);
    }

    @Override
    public void deleteById(long id) {
        stadiumRepository.deleteById(id);
    }

    @Override
    public void removeReferences(long id) {
        StadiumDAO stadiumDAO = stadiumRepository.getReferenceById(id);
        stadiumDAO.getGames().forEach(game -> {
            if (game != null) {
                game.setStadium(null);
                gameService.create(game);
            }
        });
    }
}
