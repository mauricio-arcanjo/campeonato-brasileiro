package com.mauarcanjo.campeonato_brasileiro.service;

import com.mauarcanjo.campeonato_brasileiro.dto.team.AddTeamDto;
import com.mauarcanjo.campeonato_brasileiro.dto.team.DetailsTeamDto;
import com.mauarcanjo.campeonato_brasileiro.dto.team.GetTeamDto;

public interface TeamService {

    DetailsTeamDto addTeam(AddTeamDto teamDto);
    DetailsTeamDto getTeam(GetTeamDto teamDto);
    DetailsTeamDto editTeam(Long id, AddTeamDto teamDto);
    void deleteTeam(Long id);

}
