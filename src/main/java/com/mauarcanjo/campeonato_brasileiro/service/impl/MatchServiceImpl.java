package com.mauarcanjo.campeonato_brasileiro.service.impl;

import com.mauarcanjo.campeonato_brasileiro.dto.match.AddMatchDto;
import com.mauarcanjo.campeonato_brasileiro.dto.match.DetailsMatchDto;
import com.mauarcanjo.campeonato_brasileiro.entity.Match;
import com.mauarcanjo.campeonato_brasileiro.entity.Serie;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;
import com.mauarcanjo.campeonato_brasileiro.exception.ValidationException;
import com.mauarcanjo.campeonato_brasileiro.mapper.MatchMapper;
import com.mauarcanjo.campeonato_brasileiro.repository.MatchRepository;
import com.mauarcanjo.campeonato_brasileiro.repository.TeamRepository;
import com.mauarcanjo.campeonato_brasileiro.service.MatchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

   private MatchRepository matchRepository;
   private TeamRepository teamRepository;

    public DetailsMatchDto addMatch(AddMatchDto addMatchDto) {

        Team homeTeam = getTeam(addMatchDto.homeTeamNameOrAbbreviation(), addMatchDto.serie());
        Team visitorTeam = getTeam(addMatchDto.visitorTeamNameOrAbbreviation(), addMatchDto.serie());

        validateExistingNumberOfTeams(addMatchDto.serie());

        validateIfMatchAlreadyExists(homeTeam, visitorTeam, addMatchDto.year(), addMatchDto.serie());

        Match match = MatchMapper.mapToMatch(addMatchDto, homeTeam, visitorTeam);
        Match savedMatch = matchRepository.save(match);

        return MatchMapper.mapToDetailsMatchDto(savedMatch);
    }

    public void sortAllRemainingMatches(int year, Serie serie) throws ValidationException{

        validateExistingNumberOfTeams(serie);

        List<Team> teams = teamRepository.findAllBySerie(serie);
        for (Team homeTeam : teams){

            for (Team visitorTeam : teams){

                if (!homeTeam.getName().equals(visitorTeam.getName())){

                    try {
                        validateIfMatchAlreadyExists(homeTeam, visitorTeam, year, serie);
                    } catch (ValidationException exception){
                        continue;
                    }

                    int goalsHomeTeam = (int) (Math.random() * 5.0);
                    int goalsVisitorTeam = (int) (Math.random() * 5.0);

                    Match match = new Match(
                            null,
                            homeTeam,
                            visitorTeam,
                            goalsHomeTeam,
                            goalsVisitorTeam,
                            serie,
                            year
                    );
                    matchRepository.save(match);
                }
            }
        }

    }

    private Team getTeam(String teamNameOrAbbreviation, Serie serie){
        Optional<Team> team =
                teamNameOrAbbreviation.length() == 3
                    ? teamRepository.findByAbbreviationAndSerie(teamNameOrAbbreviation, serie)
                    : teamRepository.findByNameAndSerie(teamNameOrAbbreviation, serie);

        return team.orElseThrow(()-> new ValidationException("Team " + teamNameOrAbbreviation + " not found in " + serie + " database!"));
//        return teamRepository.findByNameAndSerie(teamName, serie)
//                .orElseThrow(()-> new ValidationException("Team " + teamName + " not found in " + serie + " database!"));
    }

    private void validateIfMatchAlreadyExists(Team homeTeam, Team visitorTeam, int year, Serie serie){

        boolean matchStatus =
//                matchRepository.existsByHomeTeamAndVisitorTeam(homeTeam, visitorTeam);
                matchRepository.existsByHomeTeamAndVisitorTeamAndYearAndSerie(homeTeam, visitorTeam, year, serie);
        if (matchStatus){
            String message = """
                    Match between %s and %s in %s for the %s has already been registered!"""
                    .formatted(homeTeam.getName(), visitorTeam.getName(), year, serie);
            throw new ValidationException(message);
        }
    }

    private void validateExistingNumberOfTeams(Serie serie){
        long teamsCount = teamRepository.countBySerie(serie);
        if (teamsCount < 20){
            throw new ValidationException("It's needed to have 20 teams to start to register matches in " + serie + "!");
        }
    }

}
