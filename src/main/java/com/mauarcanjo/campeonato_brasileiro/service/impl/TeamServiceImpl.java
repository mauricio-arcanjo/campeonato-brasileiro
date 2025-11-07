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

        if (teamDto.name() == null || teamDto.abbreviation() == null || teamDto.state() == null){
            throw new ValidationException("Team name, abbreviation or state can't be null!");
        }

        long count = teamRepository.count();

        if (count <= 20){
            validateName(teamDto.name());
            validateAbbreviation(teamDto.abbreviation());
            validateState(teamDto.state());

            Team team = TeamMapper.mapToTeam(teamDto);
            Team savedTeam = teamRepository.save(team);
            return  TeamMapper.mapToDetailsTeamDto(savedTeam);
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
        Team team = getTeamById(id);

        if (teamDto.name() != null && !team.getName().equals(teamDto.name())) {
            validateName(teamDto.name());
            team.setName(teamDto.name());
        }

        if (teamDto.abbreviation() != null && !team.getAbbreviation().equals(teamDto.abbreviation())) {
            validateAbbreviation(teamDto.abbreviation());
            team.setAbbreviation(teamDto.abbreviation().toUpperCase());
        }

        if (teamDto.state() != null){
            validateState(teamDto.state());
            team.setState(teamDto.state().toUpperCase());
        }

        return TeamMapper.mapToDetailsTeamDto(team);
    }

    public void deleteTeam(Long id) {
        Team team = getTeamById(id);
        teamRepository.deleteById(id);
    }

    private Team getTeamById(Long id){
        return teamRepository.findById(id).orElseThrow(() -> new ValidationException("Id doesn't exists!"));
    }

    private void validateName(String name){
        boolean nameStatus = teamRepository.existsByName(name);
        if (nameStatus) {
            throw new ValidationException("Team name is already in use!");
        }
    }

    private void validateAbbreviation(String abbreviation){
        if (abbreviation.length() != 3){
            throw new ValidationException("Abbreviation need to have 3 chars!");
        }
        boolean abbreviationStatus = teamRepository.existsByAbbreviation(abbreviation);
        if (abbreviationStatus) {
            throw new ValidationException("Team abbreviation is already in use!");
        }
    }

    private void validateState(String state) {
        if (state.length() != 2) {
            throw new ValidationException("State need to have 2 chars!");
        }
    }



}
