package com.mauarcanjo.campeonato_brasileiro.service.impl;

import com.mauarcanjo.campeonato_brasileiro.dto.match.AddMatchDto;
import com.mauarcanjo.campeonato_brasileiro.dto.match.DetailsMatchDto;
import com.mauarcanjo.campeonato_brasileiro.entity.Match;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;
import com.mauarcanjo.campeonato_brasileiro.exception.ValidationException;
import com.mauarcanjo.campeonato_brasileiro.mapper.MatchMapper;
import com.mauarcanjo.campeonato_brasileiro.repository.MatchRepository;
import com.mauarcanjo.campeonato_brasileiro.repository.TeamRepository;
import com.mauarcanjo.campeonato_brasileiro.service.MatchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

   private MatchRepository matchRepository;
   private TeamRepository teamRepository;

    public DetailsMatchDto addMatch(AddMatchDto addMatchDto) {

        Team homeTeam = getTeam(addMatchDto.homeTeamName());
        Team visitorTeam = getTeam(addMatchDto.visitorTeamName());

        validateExistingNumberOfTeams();

        validateIfMatchAlreadyExists(homeTeam, visitorTeam, addMatchDto.year());

        Match match = MatchMapper.mapToMatch(addMatchDto, homeTeam, visitorTeam);
        Match savedMatch = matchRepository.save(match);

        return MatchMapper.mapToDetailsMatchDto(savedMatch);
    }

    public void sortAllRemainingMatches(int year) throws ValidationException{

        validateExistingNumberOfTeams();

        List<Team> teams = teamRepository.findAll();
        for (Team homeTeam : teams){

            for (Team visitorTeam : teams){
                if (!homeTeam.getName().equals(visitorTeam.getName())){

                    try {
                        validateIfMatchAlreadyExists(homeTeam, visitorTeam, year);
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
                            year
                    );

                    matchRepository.save(match);
                }
            }
        }

    }

    private Team getTeam(String teamName){
        return teamRepository.findByName(teamName)
                .orElseThrow(()-> new ValidationException("Team " + teamName + " not found in database!"));
    }

    private void validateIfMatchAlreadyExists(Team homeTeam, Team visitorTeam, int year){

        boolean matchStatus =
//                matchRepository.existsByHomeTeamAndVisitorTeam(homeTeam, visitorTeam);
                matchRepository.existsByHomeTeamAndVisitorTeamAndYear(homeTeam, visitorTeam, year);
        if (matchStatus){
            String message = """
                    Match between %s and %s has already been registered!
                    """.formatted(homeTeam.getName(), visitorTeam.getName());
            throw new ValidationException(message);
        }
    }

    private void validateExistingNumberOfTeams(){
        long teamsCount = teamRepository.count();
        if (teamsCount < 20){
            throw new ValidationException("It's needed to have 20 teams to start to register matches!");
        }
    }

}
