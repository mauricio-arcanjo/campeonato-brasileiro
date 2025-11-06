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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService {

    private TeamRepository teamRepository;

    public DetailsTeamDto addTeam(AddTeamDto teamDto) {

//        boolean teamExists = teamRepository.existsByNameOrAbbreviation(teamDto.name(), teamDto.abbreviation());
//
//        if (teamExists){
//            throw new ValidationException("Team already exists!");
//        }
        checkIfTeamExists(teamDto);

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

    public List<DetailsTeamDto> listTeams(){
        List<Team> teams = teamRepository.findAll();
        return teams.stream()
                .map(TeamMapper::mapToDetailsTeamDto)
                .toList();
    }

    @Transactional
    public DetailsTeamDto editTeam(Long id, AddTeamDto teamDto) {
        Team team = getTeamById(id, "Id doesn't exists!");

        if (!team.getName().equals(teamDto.name())) {
            boolean nameStatus = teamRepository.existsByName(teamDto.name());
            if (nameStatus) {
                throw new ValidationException("Team name is already in use!");
            }
        }
        if (!team.getAbbreviation().equals(teamDto.abbreviation())) {
            boolean abbreviationStatus = teamRepository.existsByAbbreviation(teamDto.abbreviation());
            if (abbreviationStatus) {
                throw new ValidationException("Team abbreviation is already in use!");
            }
        }
        if (teamDto.name() != null){
            team.setName(teamDto.name());
        }
        if (teamDto.abbreviation() != null){
            team.setAbbreviation(teamDto.abbreviation().toUpperCase());
        }
        if (teamDto.state() != null){
            team.setState(teamDto.state().toUpperCase());
        }

        return TeamMapper.mapToDetailsTeamDto(team);
    }

    public void deleteTeam(Long id) {
        Team team = getTeamById(id, "Id doesn't exists!");
        teamRepository.deleteById(id);
    }

    private void checkIfTeamExists(AddTeamDto teamDto){

        boolean teamExists = teamRepository.existsByNameOrAbbreviation(teamDto.name(), teamDto.abbreviation());

        if (teamExists){
            throw new ValidationException("Team already exists!");
        }
    }

    private Team getTeamById(Long id, String message){
        return teamRepository.findById(id).orElseThrow(() -> new ValidationException(message));
    }


}
