package com.netrom.netromfootballmanager.mappers;

import com.netrom.netromfootballmanager.entities.daos.*;
import com.netrom.netromfootballmanager.entities.dtos.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class Mapper {

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

    public GameDAO DTOToDAO(GameDTO gameDTO) {
        return new GameDAO(
                gameDTO.getId(),
                gameDTO.getDateAndTimeInMillis(),
                gameDTO.getTeamOneId() == null ? null : new TeamDAO() {{
                    setId(gameDTO.getTeamOneId());
                }},
                gameDTO.getTeamTwoId() == null ? null : new TeamDAO() {{
                    setId(gameDTO.getTeamTwoId());
                }},
                gameDTO.getStadiumId() == null ? null : new StadiumDAO() {{
                    setId(gameDTO.getStadiumId());
                }},
                gameDTO.getGameResultId() == null ? null : new GameResultDAO() {{
                    setId(gameDTO.getGameResultId());
                }}
        );
    }

    public GameResultDAO DTOToDAO(GameResultDTO gameResultDTO) {
        return new GameResultDAO(
                gameResultDTO.getId(),
                gameResultDTO.getGoalsTeamOne(),
                gameResultDTO.getGoalsTeamTwo(),
                gameResultDTO.getGameId() == null ? null : new GameDAO() {{
                    setId(gameResultDTO.getGameId());
                }}
        );
    }

    public PlayerDAO DTOToDAO(PlayerDTO playerDTO) {
        return new PlayerDAO(playerDTO.getId(),
                playerDTO.getName(),
                playerDTO.getGoalsScored(),
                playerDTO.getRole(),
                playerDTO.getTeamId() == null ? null : new TeamDAO() {{
                    setId(playerDTO.getTeamId());
                }}
        );
    }

    public StadiumDAO DTOToDAO(StadiumDTO stadiumDTO) {
        return new StadiumDAO(
                stadiumDTO.getId(),
                stadiumDTO.getName(),
                stadiumDTO.getLocation(),
                stadiumDTO.getGamesIds().stream().map(gameId -> gameId == null ? null : new GameDAO() {{
                    setId(gameId);
                }}).collect(Collectors.toList())
        );
    }

    public TeamDAO DTOToDAO(TeamDTO teamDTO) {
        return new TeamDAO(
                teamDTO.getId(),
                teamDTO.getName(),
                teamDTO.getGoalsScored(),
                teamDTO.getGoalsReceived(),
                teamDTO.getVictories(),
                teamDTO.getDefeats(),
                teamDTO.getDraws(),
                teamDTO.getPlayersIds().stream().map(playerId -> playerId == null ? null : new PlayerDAO() {{
                    setId(playerId);
                }}).collect(Collectors.toList()),
                teamDTO.getGamesAsTeamOneIds().stream().map(gameId -> gameId == null ? null : new GameDAO() {{
                    setId(gameId);
                }}).collect(Collectors.toList()),
                teamDTO.getGamesAsTeamTwoIds().stream().map(gameId -> gameId == null ? null : new GameDAO() {{
                    setId(gameId);
                }}).collect(Collectors.toList())
        );
    }
}
