package com.mauarcanjo.campeonato_brasileiro.service.impl;

import com.mauarcanjo.campeonato_brasileiro.entity.*;
import com.mauarcanjo.campeonato_brasileiro.exception.ValidationException;
import com.mauarcanjo.campeonato_brasileiro.repository.LeagueTableRepository;
import com.mauarcanjo.campeonato_brasileiro.repository.MatchRepository;
import com.mauarcanjo.campeonato_brasileiro.repository.TeamRepository;
import com.mauarcanjo.campeonato_brasileiro.service.LeagueTableService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@AllArgsConstructor
@Service
public class LeagueTableServiceImpl implements LeagueTableService {

    private LeagueTableRepository leagueTableRepository;
    private MatchRepository matchRepository;
    private TeamRepository teamRepository;

    public void fillUpTable(int year, Serie serie) {
        validateNumberOfMatches(year, serie);
        createTableForAllTeams(year, serie);
        List<Match> matches = matchRepository.findAllByYearAndSerie(year, serie);

        for (Match match : matches){
            updateTeamsInTable(match);
        }
    }

    @Transactional
    public void completeTable(int year, Serie serie){
        validateNumberOfMatches(year, serie);
        List<LeagueTable> leagueTablePositions = leagueTableRepository.findAllByYearAndSerie(year, serie);
        leagueTablePositions.forEach(LeagueTable::complete);
    }

    @Transactional
    public void relegateLastFourQualified(int year, Serie serie) {
        validadeIfTableStatusIsSetToComplete(year, serie);
        List<LeagueTable> lastPositions = leagueTableRepository.findTop4ByYearAndSerieOrderByPointsAsc(year, serie);

        for (LeagueTable position : lastPositions){
            position.getTeam().relegate();
        }

    }



    private void createTableForAllTeams(int year, Serie serie) {
        List<Team> teams = teamRepository.findAllBySerie(serie);
         for (Team team : teams){
             LeagueTable teamTable = new LeagueTable(team, year, serie);
             leagueTableRepository.save(teamTable);
         }
    }

    private void validateNumberOfMatches(int year, Serie serie){
        long numberOfMatches = matchRepository.countByYearAndSerie(year, serie);
        if (numberOfMatches != 380){
            throw new ValidationException("All 380 matches needs to be added to be able to fill up league table!");
        }
    }


    @Transactional
    private void updateTeamsInTable(Match match){
        LeagueTable homeTeamTable = leagueTableRepository.findByTeam(match.getHomeTeam());
        LeagueTable visitorTeamTable = leagueTableRepository.findByTeam(match.getVisitorTeam());

        if (match.getGoalsHomeTeam() > match.getGoalsVisitorTeam()){
            homeTeamTable.setResult(MatchResults.WIN, match.getGoalsHomeTeam(), match.getGoalsVisitorTeam());
            visitorTeamTable.setResult(MatchResults.LOOSE, match.getGoalsVisitorTeam(), match.getGoalsHomeTeam());
        } else if  (match.getGoalsHomeTeam() == match.getGoalsVisitorTeam()){
            homeTeamTable.setResult(MatchResults.DRAW, match.getGoalsHomeTeam(), match.getGoalsVisitorTeam());
            visitorTeamTable.setResult(MatchResults.DRAW, match.getGoalsVisitorTeam(), match.getGoalsHomeTeam());
        } else {
            homeTeamTable.setResult(MatchResults.LOOSE, match.getGoalsHomeTeam(), match.getGoalsVisitorTeam());
            visitorTeamTable.setResult(MatchResults.WIN, match.getGoalsVisitorTeam(), match.getGoalsHomeTeam());
        }
        leagueTableRepository.save(homeTeamTable);
        leagueTableRepository.save(visitorTeamTable);
    }

    private void validadeIfTableStatusIsSetToComplete(int year, Serie serie){
        long count = leagueTableRepository.countByYearAndSerieAndStatusNot(
                year, serie, LeagueStatus.COMPLETED);

        if (count != 0){
            throw new ValidationException("League table must be set to completed before relegating teams!");
        }
    }

}
