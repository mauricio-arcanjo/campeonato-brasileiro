package com.mauarcanjo.campeonato_brasileiro.service.impl;

import com.mauarcanjo.campeonato_brasileiro.entity.LeagueTable;
import com.mauarcanjo.campeonato_brasileiro.entity.Match;
import com.mauarcanjo.campeonato_brasileiro.entity.MatchResults;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;
import com.mauarcanjo.campeonato_brasileiro.exception.ValidationException;
import com.mauarcanjo.campeonato_brasileiro.repository.LeagueTableRepository;
import com.mauarcanjo.campeonato_brasileiro.repository.MatchRepository;
import com.mauarcanjo.campeonato_brasileiro.repository.TeamRepository;
import com.mauarcanjo.campeonato_brasileiro.service.LeagueTableService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class LeagueTableServiceImpl implements LeagueTableService {

    private LeagueTableRepository leagueTableRepository;
    private MatchRepository matchRepository;
    private TeamRepository teamRepository;

    public void fillUpTable(int year) {
        validateNumberOfMatches(year);
        createTableForAllTeams(year);
        List<Match> matches = matchRepository.findAllByYear(year);

        for (Match match : matches){
            updateTeamsInTable(match);
        }

    }

    private void createTableForAllTeams(int year) {

        List<Team> teams = teamRepository.findAll();
         for (Team team : teams){
             LeagueTable teamTable = new LeagueTable(team, year);
             leagueTableRepository.save(teamTable);
         }

    }

    private void validateNumberOfMatches(int year){
        long numberOfMatches = matchRepository.countByYear(year);
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



}
