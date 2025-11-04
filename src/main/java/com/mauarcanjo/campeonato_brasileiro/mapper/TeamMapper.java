package com.mauarcanjo.campeonato_brasileiro.mapper;

import com.mauarcanjo.campeonato_brasileiro.dto.team.AddTeamDto;
import com.mauarcanjo.campeonato_brasileiro.dto.team.DetailsTeamDto;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;

public class TeamMapper {

    public static Team mapToTeam (AddTeamDto teamDto){
        return new Team(null,
                teamDto.name(),
                teamDto.abbreviation(),
                teamDto.state()
        );
    }

    public static DetailsTeamDto mapToDetailsTeamDto(Team team){
        return new DetailsTeamDto(
                team.getId(),
                team.getName(),
                team.getAbbreviation(),
                team.getState()
        );
    }

}
