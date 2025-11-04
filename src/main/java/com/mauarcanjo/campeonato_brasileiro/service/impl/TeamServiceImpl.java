package com.mauarcanjo.campeonato_brasileiro.service.impl;

import com.mauarcanjo.campeonato_brasileiro.dto.team.AddTeamDto;
import com.mauarcanjo.campeonato_brasileiro.dto.team.DetailsTeamDto;
import com.mauarcanjo.campeonato_brasileiro.dto.team.GetTeamDto;
import com.mauarcanjo.campeonato_brasileiro.exception.ValidationException;
import com.mauarcanjo.campeonato_brasileiro.mapper.TeamMapper;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;
import com.mauarcanjo.campeonato_brasileiro.repository.TeamRepository;
import com.mauarcanjo.campeonato_brasileiro.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService {

    private TeamRepository teamRepository;

    public DetailsTeamDto addTeam(AddTeamDto teamDto) {

        boolean teamExists = teamRepository.existsByNameOrAbbreviation(teamDto.name(), teamDto.abbreviation());

        if (teamExists){
            throw new ValidationException("Team already exists!");
        }

        long count = teamRepository.count();

        if (count <= 20){
            Team team = TeamMapper.mapToTeam(teamDto);
            team = teamRepository.save(team);
            return  TeamMapper.mapToDetailsTeamDto(team);
        } else {
            throw new ValidationException("Reached maximum quantity of 20 teams!");
        }
    }

    public DetailsTeamDto getTeam(GetTeamDto teamDto) {

        Team team = teamRepository
                .findByNameOrAbbreviation(teamDto.name(), teamDto.abbreviation()).orElseThrow();

        return TeamMapper.mapToDetailsTeamDto(team);
    }




}
