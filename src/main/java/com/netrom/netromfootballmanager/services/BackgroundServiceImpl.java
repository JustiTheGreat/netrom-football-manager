package com.netrom.netromfootballmanager.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Transactional
@Service
public class BackgroundServiceImpl {

    @Autowired
    private GameService gameService;

    @Scheduled(fixedRate = 5000)
    public void generateRandomResultsForAllGames() {
        gameService.getList().forEach(gameDAO -> {
            long currentTime = new Date().getTime();
            if (gameDAO.getGameResult() == null && gameDAO.getDateAndTimeInMillis() < currentTime) {
                gameService.generateRandomGameResult(gameDAO.getId());
            }
        });
    }
}
