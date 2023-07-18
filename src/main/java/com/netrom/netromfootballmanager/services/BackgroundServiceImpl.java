package com.netrom.netromfootballmanager.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
            System.err.println(gameDAO.getDateAndTimeInMillis() + "<" + currentTime);
            DateFormat obj = new SimpleDateFormat("dd MM yyyy HH:mm:ss:SSS Z");
            // we create instance of the Date and pass milliseconds to the constructor
            System.err.println(obj.format(new Date(gameDAO.getDateAndTimeInMillis())));
            //System.out.println(obj.format(currentTime));
            System.err.println(gameDAO.getDateAndTimeInMillis() < currentTime);
            if (gameDAO.getGameResult() == null && gameDAO.getDateAndTimeInMillis() < currentTime) {
                gameService.generateRandomGameResult(gameDAO.getId());
            }
        });
    }
}
