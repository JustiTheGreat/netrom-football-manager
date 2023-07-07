package com.netrom.netromfootballmanager.mappers;

import com.netrom.netromfootballmanager.entities.daos.*;
import com.netrom.netromfootballmanager.entities.dtos.*;
import com.netrom.netromfootballmanager.services.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

public class Mapper {

    @Autowired
    private static GameService gameService;

    @Autowired
    private static GameResultService gameResultService;

    @Autowired
    private static PlayerService playerService;

    @Autowired
    private static StadiumService stadiumService;

    @Autowired
    private static TeamService teamService;

    public static GameDTO DAOToDTO(GameDAO gameDAO) {
        return new GameDTO(
                gameDAO.getId(),
                gameDAO.getDateAndTimeInMillis(),
                gameDAO.getTeamOneId(),
                gameDAO.getTeamTwoId(),
                gameDAO.getStadiumId(),
                gameDAO.getGameResultId()
        );
    }

    public static GameResultDTO DAOToDTO(GameResultDAO gameResultDAO) {
        return new GameResultDTO(
                gameResultDAO.getId(),
                gameResultDAO.getGoalsTeamOne(),
                gameResultDAO.getGoalsTeamTwo(),
                gameResultDAO.getGameId()
        );
    }

    public static PlayerDTO DAOToDTO(PlayerDAO playerDAO) {
        return new PlayerDTO(playerDAO.getId(),
                playerDAO.getName(),
                playerDAO.getGoalsScored(),
                playerDAO.getRole(),
                playerDAO.getTeamId()
        );
    }

    public static StadiumDTO DAOToDTO(StadiumDAO stadiumDAO) {
        return new StadiumDTO(
                stadiumDAO.getId(),
                stadiumDAO.getName(),
                stadiumDAO.getLocation(),
                stadiumDAO.getGamesIds()
        );
    }

    public static TeamDTO DAOToDTO(TeamDAO teamDAO) {
        return new TeamDTO(
                teamDAO.getId(),
                teamDAO.getName(),
                teamDAO.getGoalsScored(),
                teamDAO.getGoalsReceived(),
                teamDAO.getVictories(),
                teamDAO.getDefeats(),
                teamDAO.getDraws(),
                teamDAO.getPlayersIds(),
                teamDAO.getGamesAsTeamOneIds(),
                teamDAO.getGamesAsTeamTwoIds()
        );
    }

    public static GameDAO DTOToDAO(GameDTO gameDTO) {
        return new GameDAO(
                gameDTO.getId(),
                gameDTO.getDateAndTimeInMillis(),
                teamService.getById(gameDTO.getTeamOneId()),
                teamService.getById(gameDTO.getTeamTwoId()),
                stadiumService.getById(gameDTO.getStadiumId()),
                gameResultService.getById(gameDTO.getGameResultId())
        );
    }

    public static GameResultDAO DTOToDAO(GameResultDTO gameResultDTO) {
        return new GameResultDAO(
                gameResultDTO.getId(),
                gameResultDTO.getGoalsTeamOne(),
                gameResultDTO.getGoalsTeamTwo(),
                gameService.getById(gameResultDTO.getGameId())
        );
    }

    public static PlayerDAO DTOToDAO(PlayerDTO playerDTO) {
        return new PlayerDAO(playerDTO.getId(),
                playerDTO.getName(),
                playerDTO.getGoalsScored(),
                playerDTO.getRole(),
                teamService.getById(playerDTO.getTeamId())
        );
    }

    public static StadiumDAO DTOToDAO(StadiumDTO stadiumDTO) {
        return new StadiumDAO(
                stadiumDTO.getId(),
                stadiumDTO.getName(),
                stadiumDTO.getLocation(),
                stadiumDTO.getGamesIds().stream().map(gameId -> gameService.getById(gameId)).collect(Collectors.toList())
        );
    }

    public static TeamDAO DTOToDAO(TeamDTO teamDTO) {
        return new TeamDAO(
                teamDTO.getId(),
                teamDTO.getName(),
                teamDTO.getGoalsScored(),
                teamDTO.getGoalsReceived(),
                teamDTO.getVictories(),
                teamDTO.getDefeats(),
                teamDTO.getDraws(),
                teamDTO.getPlayersIds().stream().map(playerId -> playerService.getById(playerId)).collect(Collectors.toList()),
                teamDTO.getGamesAsTeamOneIds().stream().map(gameId -> gameService.getById(gameId)).collect(Collectors.toList()),
                teamDTO.getGamesAsTeamTwoIds().stream().map(gameId -> gameService.getById(gameId)).collect(Collectors.toList())
        );
    }
}
