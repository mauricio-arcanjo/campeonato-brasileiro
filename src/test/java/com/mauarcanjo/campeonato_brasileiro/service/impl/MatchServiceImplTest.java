package com.mauarcanjo.campeonato_brasileiro.service.impl;

import com.mauarcanjo.campeonato_brasileiro.dto.match.AddMatchDto;
import com.mauarcanjo.campeonato_brasileiro.dto.match.DetailsMatchDto;
import com.mauarcanjo.campeonato_brasileiro.entity.Match;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;
import com.mauarcanjo.campeonato_brasileiro.exception.ValidationException;
import com.mauarcanjo.campeonato_brasileiro.repository.MatchRepository;
import com.mauarcanjo.campeonato_brasileiro.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchServiceImplTest {

    @InjectMocks
    private MatchServiceImpl matchService;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamRepository teamRepository;

    private AddMatchDto addMatchDto;
    private Team homeTeam = new Team(1L, "Bahia", "BAH", "BA");
    private Team visitorTeam = new Team(2L, "Ceara", "CEA", "CE");
    private Match match = new Match(1L, homeTeam, visitorTeam, 3, 1, 2025);

    @Captor
    private ArgumentCaptor<Match> matchCaptor;

    @Test
    void shouldntAddMatchDueToTeamValidationFail(){
        //Arrange
        this.addMatchDto =
                new AddMatchDto("Bahia",
                        "Ceara",
                        3,
                        1,
                        2025);

        // Act
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> matchService.addMatch(this.addMatchDto)
        );

        // Assert: checar a mensagem
        System.out.println(exception.getMessage());
        Assertions.assertEquals("Team " + addMatchDto.homeTeamName() + " not found in database!", exception.getMessage());
    }

    @Test
    void shouldntAddMatchDueToNotHavingEnoughTeams(){
        //Arrange
        this.addMatchDto =
                new AddMatchDto("Bahia",
                        "Ceara",
                        3,
                        1,
                        2025);

        when(teamRepository.findByName("Bahia")).thenReturn(Optional.ofNullable(homeTeam));
        when(teamRepository.findByName("Ceara")).thenReturn(Optional.ofNullable(visitorTeam));
        when(teamRepository.count()).thenReturn(5L);
        // Act
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> matchService.addMatch(this.addMatchDto)
        );

        // Assert: checar a mensagem
        System.out.println(exception.getMessage());
        Assertions.assertEquals("It's needed to have 20 teams to start to register matches!", exception.getMessage());
    }

    @Test
    void shouldAddMatchSuccessfully(){
        //Arrange
        this.addMatchDto =
                new AddMatchDto("Bahia",
                        "Ceara",
                        3,
                        1,
                        2025);

        when(teamRepository.findByName("Bahia")).thenReturn(Optional.ofNullable(homeTeam));
        when(teamRepository.findByName("Ceara")).thenReturn(Optional.ofNullable(visitorTeam));
        when(teamRepository.count()).thenReturn(20L);
        when(matchRepository.save(any())).thenReturn(match);
        // Act
        DetailsMatchDto savedMatch = matchService.addMatch(addMatchDto);

        // Assert
        Assertions.assertDoesNotThrow(() -> matchService.addMatch(this.addMatchDto));
        Assertions.assertEquals(savedMatch.homeTeam().getName(), addMatchDto.homeTeamName());
        Assertions.assertEquals(savedMatch.visitorTeam().getName(), addMatchDto.visitorTeamName());
        Assertions.assertEquals(savedMatch.goalsHomeTeam(), addMatchDto.goalsHomeTeam());
        Assertions.assertEquals(savedMatch.goalsVisitorTeam(), addMatchDto.goalsVisitorTeam());
        }



}

