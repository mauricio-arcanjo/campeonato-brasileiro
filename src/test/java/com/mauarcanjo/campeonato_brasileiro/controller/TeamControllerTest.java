package com.mauarcanjo.campeonato_brasileiro.controller;

import com.mauarcanjo.campeonato_brasileiro.dto.team.AddTeamDto;
import com.mauarcanjo.campeonato_brasileiro.dto.team.DetailsTeamDto;
import com.mauarcanjo.campeonato_brasileiro.repository.TeamRepository;
import com.mauarcanjo.campeonato_brasileiro.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(TeamController.class)
@AutoConfigureJsonTesters
public class TeamControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<AddTeamDto> addTeamDtoJacksonTester;

    @Autowired
    private JacksonTester<DetailsTeamDto> detailsTeamDtoJacksonTester;

    @Autowired
    private JacksonTester<List<DetailsTeamDto>> detailsTeamDtoListJacksonTester;

    @MockitoBean
    private TeamServiceImpl teamService;

    @MockitoBean
    private TeamRepository teamRepository;

    @Test
    void shouldReturnError400DueToLackOfAddTeamDto() throws Exception{
        //Arrange
        String json = "";
        //Act
        var response = mvc.perform(
                post("/api/teams")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //Assert
        assertEquals(400, response.getStatus());
    }

    @Test
    void shouldReturnCode200ForCorrectAddTeamRequest() throws Exception{
        //Arrange
        AddTeamDto request = new AddTeamDto("Bahia", "BAH", "BA");
        DetailsTeamDto expectedResponse = new DetailsTeamDto(1L, "Bahia", "BAH", "BA");

        Mockito.when(teamService.addTeam(request)).thenReturn(expectedResponse);
        //Act
        MockHttpServletResponse response = mvc.perform(
                post("/api/teams")
                        .content(addTeamDtoJacksonTester.write(request).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        assertEquals(200, response.getStatus());

        DetailsTeamDto jsonResponse =
                detailsTeamDtoJacksonTester.parseObject(response.getContentAsString());

        assertEquals(expectedResponse.id(), jsonResponse.id());
        assertEquals(expectedResponse.name(), jsonResponse.name());
        assertEquals(expectedResponse.abbreviation(), jsonResponse.abbreviation());
        assertEquals(expectedResponse.state(), jsonResponse.state());
    }

    @Test
    void shouldReturnCode200AndAListOfTeams() throws Exception{
        //Arrange
        List<DetailsTeamDto> expectedResult = new ArrayList<>();
        Mockito.when(teamService.listTeams()).thenReturn(expectedResult);

        //Act
        var response = mvc.perform(
                get("/api/teams/all")
        ).andReturn().getResponse();

        //Assert
        assertEquals(200, response.getStatus());

        List<DetailsTeamDto> responseList = detailsTeamDtoListJacksonTester
                .parseObject(response.getContentAsString());

        assertEquals(expectedResult, responseList);
    }

    @Test
    void shouldReturnCode200AndATeamDto() throws Exception{
        //Arrange
        AddTeamDto modificatedTeam = new AddTeamDto("BBMP", "BBM", "BA");
        DetailsTeamDto expectedResponse = new DetailsTeamDto(1L, "BBMP", "BBM", "BA");
        Mockito.when(teamService.editTeam(1L, modificatedTeam)).thenReturn(expectedResponse);
        //Act
        var response = mvc.perform(
                patch("/api/teams/1")
                        .content(addTeamDtoJacksonTester.write(modificatedTeam).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //Assert
        assertEquals(200, response.getStatus());

        DetailsTeamDto responseDetailDto = detailsTeamDtoJacksonTester
                .parseObject(response.getContentAsString());

        assertEquals(modificatedTeam.name(), responseDetailDto.name());
        assertEquals(modificatedTeam.abbreviation(), responseDetailDto.abbreviation());
        assertEquals(modificatedTeam.state(), responseDetailDto.state());

    }

}
