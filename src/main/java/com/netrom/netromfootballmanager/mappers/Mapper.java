package com.netrom.netromfootballmanager.mappers;

import com.netrom.netromfootballmanager.entities.daos.*;
import com.netrom.netromfootballmanager.entities.dtos.*;
import com.netrom.netromfootballmanager.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class Mapper {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameResultService gameResultService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private StadiumService stadiumService;

    @Autowired
    private TeamService teamService;

    public GameDTO DAOToDTO(GameDAO gameDAO) {
        return new GameDTO(
                gameDAO.getId(),
                gameDAO.getDateAndTimeInMillis(),
                gameDAO.getTeamOneId(),
                gameDAO.getTeamTwoId(),
                gameDAO.getStadiumId(),
                gameDAO.getGameResultId()
        );
    }

    public GameResultDTO DAOToDTO(GameResultDAO gameResultDAO) {
        return new GameResultDTO(
                gameResultDAO.getId(),
                gameResultDAO.getGoalsTeamOne(),
                gameResultDAO.getGoalsTeamTwo(),
                gameResultDAO.getGameId()
        );
    }

    public PlayerDTO DAOToDTO(PlayerDAO playerDAO) {
        return new PlayerDTO(playerDAO.getId(),
                playerDAO.getName(),
                playerDAO.getGoalsScored(),
                playerDAO.getRole(),
                playerDAO.getTeamId()
        );
    }

    public StadiumDTO DAOToDTO(StadiumDAO stadiumDAO) {
        return new StadiumDTO(
                stadiumDAO.getId(),
                stadiumDAO.getName(),
                stadiumDAO.getLocation(),
                stadiumDAO.getGamesIds()
        );
    }

    public TeamDTO DAOToDTO(TeamDAO teamDAO) {
        TeamDTO teamDTO = new TeamDTO(
                teamDAO.getId(),
                teamDAO.getName(),
                teamDAO.getPlayersIds(),
                teamDAO.getGamesAsTeamOneIds(),
                teamDAO.getGamesAsTeamTwoIds(),
                0,
                0,
                0,
                0,
                0
        );

        gameService.findAllByTeamOne(teamDAO).forEach(gameDAO -> {
            GameResultDAO gameResultDAO = gameDAO.getGameResult();
            if (gameResultDAO != null) {
                teamDTO.setGoalsScored(teamDTO.getGoalsScored() + gameResultDAO.getGoalsTeamOne());
                teamDTO.setGoalsReceived(teamDTO.getGoalsReceived() + gameResultDAO.getGoalsTeamTwo());
                teamDTO.setVictories(teamDTO.getVictories() + (gameResultDAO.getGoalsTeamOne() > gameResultDAO.getGoalsTeamTwo() ? 1 : 0));
                teamDTO.setDefeats(teamDTO.getVictories() + (gameResultDAO.getGoalsTeamOne() < gameResultDAO.getGoalsTeamTwo() ? 1 : 0));
                teamDTO.setDraws(teamDTO.getVictories() + (gameResultDAO.getGoalsTeamOne() == gameResultDAO.getGoalsTeamTwo() ? 1 : 0));
            }
        });

        gameService.findAllByTeamTwo(teamDAO).forEach(gameDAO -> {
            GameResultDAO gameResultDAO = gameDAO.getGameResult();
            if (gameResultDAO != null) {
                teamDTO.setGoalsScored(teamDTO.getGoalsScored() + gameResultDAO.getGoalsTeamOne());
                teamDTO.setGoalsReceived(teamDTO.getGoalsReceived() + gameResultDAO.getGoalsTeamTwo());
                teamDTO.setVictories(teamDTO.getVictories() + (gameResultDAO.getGoalsTeamTwo() > gameResultDAO.getGoalsTeamOne() ? 1 : 0));
                teamDTO.setDefeats(teamDTO.getVictories() + (gameResultDAO.getGoalsTeamTwo() < gameResultDAO.getGoalsTeamOne() ? 1 : 0));
                teamDTO.setDraws(teamDTO.getVictories() + (gameResultDAO.getGoalsTeamTwo() == gameResultDAO.getGoalsTeamOne() ? 1 : 0));
            }
        });

        return teamDTO;
    }

    public GameDAO DTOToDAO(GameDTO gameDTO) {
        return new GameDAO(
                gameDTO.getId(),
                gameDTO.getDateAndTimeInMillis(),
                gameDTO.getTeamOneId() == null ? null : teamService.getById(gameDTO.getTeamOneId()),
                gameDTO.getTeamTwoId() == null ? null : teamService.getById(gameDTO.getTeamTwoId()),
                gameDTO.getStadiumId() == null ? null : stadiumService.getById(gameDTO.getStadiumId()),
                gameDTO.getGameResultId() == null ? null : gameResultService.getById(gameDTO.getGameResultId())
        );
    }

    public GameResultDAO DTOToDAO(GameResultDTO gameResultDTO) {
        return new GameResultDAO(
                gameResultDTO.getId(),
                gameResultDTO.getGoalsTeamOne(),
                gameResultDTO.getGoalsTeamTwo(),
                gameResultDTO.getGameId() == null ? null : gameService.getById(gameResultDTO.getGameId())
        );
    }

    public PlayerDAO DTOToDAO(PlayerDTO playerDTO) {
        return new PlayerDAO(playerDTO.getId(),
                playerDTO.getName(),
                playerDTO.getGoalsScored(),
                playerDTO.getRole(),
                playerDTO.getTeamId() == null ? null : teamService.getById(playerDTO.getTeamId())
        );
    }

    public StadiumDAO DTOToDAO(StadiumDTO stadiumDTO) {
        return new StadiumDAO(
                stadiumDTO.getId(),
                stadiumDTO.getName(),
                stadiumDTO.getLocation(),
                stadiumDTO.getGamesIds() == null ? new ArrayList<>() : stadiumDTO.getGamesIds().stream().map(gameId -> gameId == null ? null : gameService.getById(gameId)).collect(Collectors.toList())
        );
    }

    public TeamDAO DTOToDAO(TeamDTO teamDTO) {
        return new TeamDAO(
                teamDTO.getId(),
                teamDTO.getName(),
                teamDTO.getPlayersIds() == null ? new ArrayList<>() : teamDTO.getPlayersIds().stream().map(playerId -> playerId == null ? null : playerService.getById(playerId)).collect(Collectors.toList()),
                teamDTO.getGamesAsTeamOneIds() == null ? new ArrayList<>() : teamDTO.getGamesAsTeamOneIds().stream().map(gameId -> gameId == null ? null : gameService.getById(gameId)).collect(Collectors.toList()),
                teamDTO.getGamesAsTeamTwoIds() == null ? new ArrayList<>() : teamDTO.getGamesAsTeamTwoIds().stream().map(gameId -> gameId == null ? null : gameService.getById(gameId)).collect(Collectors.toList())
        );
    }
}
