package com.mauarcanjo.campeonato_brasileiro.service.impl;

import com.mauarcanjo.campeonato_brasileiro.dto.team.*;
import com.mauarcanjo.campeonato_brasileiro.exception.ValidationException;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;
import com.mauarcanjo.campeonato_brasileiro.mapper.TeamMapper;
import com.mauarcanjo.campeonato_brasileiro.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest {

    @InjectMocks
    private TeamServiceImpl teamService;

    @Mock
    private TeamRepository teamRepository;

    private AddTeamDto addTeamDto;
    private Team team;

    @Captor
    private ArgumentCaptor<Team> teamCaptor;


    @Test
    @DisplayName("Shouldn't add team due to maximum number of teams have been reached")
    void shouldntAddTeamDueToTeamLimitOver20(){
        //Arrange
        this.addTeamDto = new AddTeamDto("Bahia", "BAH", "BA");
        given(teamRepository.count()).willReturn(21L);

        //Act and Assert
        Assertions.assertThrows(ValidationException.class, () -> teamService.addTeam(this.addTeamDto));
    }

    @Test
    @DisplayName("Shouldn't add team due to null attributes of AddTeamDto")
    void shouldntAddTeamDueToNullAtrributes(){
        //Arrange
        this.addTeamDto = new AddTeamDto(null, null, null);

        //Act and Assert
        Assertions.assertThrows(ValidationException.class, () -> teamService.addTeam(this.addTeamDto));
    }

    @Test
    @DisplayName("Should't add team due to invalid name")
    void shouldAddTeamDueToInvalidName(){
        //Arrange
        this.addTeamDto = new AddTeamDto("Bahia", "BAH", "BA");
        given(teamRepository.count()).willReturn(15L);
        given(teamRepository.existsByName(addTeamDto.name())).willReturn(true);

        //Act and Assert
        Assertions.assertThrows(ValidationException.class, () -> teamService.addTeam(this.addTeamDto));

    }

    @Test
    @DisplayName("Should't add team due to invalid abbreviation")
    void shouldAddTeamDueToInvalidAbbreviation(){
        //Arrange
        this.addTeamDto = new AddTeamDto("Bahia", "BAH", "BA");
        given(teamRepository.count()).willReturn(15L);
        given(teamRepository.existsByName(addTeamDto.name())).willReturn(false);
        given(teamRepository.existsByAbbreviation(addTeamDto.abbreviation())).willReturn(true);

        //Act and Assert
        Assertions.assertThrows(ValidationException.class, () -> teamService.addTeam(this.addTeamDto));

    }

    @Test
    @DisplayName("Should't add team due to invalid state")
    void shouldAddTeamDueToInvalidState(){
        //Arrange
        this.addTeamDto = new AddTeamDto("Bahia", "BAH", "");
        given(teamRepository.count()).willReturn(15L);
        given(teamRepository.existsByName(addTeamDto.name())).willReturn(false);
        given(teamRepository.existsByAbbreviation(addTeamDto.abbreviation())).willReturn(false);

        //Act and Assert
        Assertions.assertThrows(ValidationException.class, () -> teamService.addTeam(this.addTeamDto));
    }

    @Test
    @DisplayName("Should add team successfully")
    void shouldAddTeamSuccessfully(){
        //Arrange
        this.addTeamDto = new AddTeamDto("Bahia", "BAH", "BA");
        this.team = TeamMapper.mapToTeam(this.addTeamDto);
        this.team.setId(1L);
        given(teamRepository.count()).willReturn(15L);
        given(teamRepository.existsByName(addTeamDto.name())).willReturn(false);
        given(teamRepository.existsByAbbreviation(addTeamDto.abbreviation())).willReturn(false);
        given(teamRepository.save(any(Team.class))).willReturn(this.team);

        //Act and Assert
        Assertions.assertDoesNotThrow(() ->teamService.addTeam(this.addTeamDto));

        then(teamRepository).should().save(teamCaptor.capture());
        Team savedTeam = teamCaptor.getValue();
        Assertions.assertEquals(addTeamDto.name(), savedTeam.getName());
        Assertions.assertEquals(addTeamDto.abbreviation(), savedTeam.getAbbreviation());
        Assertions.assertEquals(addTeamDto.state(), savedTeam.getState());
    }


}
