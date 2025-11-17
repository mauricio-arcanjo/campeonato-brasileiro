package com.mauarcanjo.campeonato_brasileiro.mapper;

import com.mauarcanjo.campeonato_brasileiro.dto.match.AddMatchDto;
import com.mauarcanjo.campeonato_brasileiro.dto.match.DetailsMatchDto;
import com.mauarcanjo.campeonato_brasileiro.entity.Match;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;

public class MatchMapper {

    public static Match mapToMatch(AddMatchDto addMatchDto, Team homeTeam, Team visitorTeam){
        return new Match(
                null,
                homeTeam,
                visitorTeam,
                addMatchDto.goalsHomeTeam(),
                addMatchDto.goalsVisitorTeam(),
                addMatchDto.serie(),
                addMatchDto.year()
        );
    }

    public static DetailsMatchDto mapToDetailsMatchDto(Match match){
        return new DetailsMatchDto(
                match.getId(),
                match.getHomeTeam(),
                match.getVisitorTeam(),
                match.getGoalsHomeTeam(),
                match.getGoalsVisitorTeam(),
                match.getSerie(),
                match.getYear()
        );
    }

}
