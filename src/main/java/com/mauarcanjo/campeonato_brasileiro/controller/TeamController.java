package com.mauarcanjo.campeonato_brasileiro.controller;

import com.mauarcanjo.campeonato_brasileiro.dto.team.AddTeamDto;
import com.mauarcanjo.campeonato_brasileiro.dto.team.DetailsTeamDto;
import com.mauarcanjo.campeonato_brasileiro.dto.team.GetTeamDto;
import com.mauarcanjo.campeonato_brasileiro.service.impl.TeamServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/teams")
public class TeamController {

    private TeamServiceImpl teamService;

    @PostMapping
    public ResponseEntity<DetailsTeamDto> addTeam(@RequestBody AddTeamDto addTeamDto){

        DetailsTeamDto detailsTeamDto = teamService.addTeam(addTeamDto);

        return ResponseEntity.ok(detailsTeamDto);
    }

    @GetMapping
    public ResponseEntity<DetailsTeamDto> getTeamByNameOrAbbreviation(@RequestBody GetTeamDto getTeamDto){

        DetailsTeamDto detailsTeamDto = teamService.getTeam(getTeamDto);

        return ResponseEntity.ok(detailsTeamDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DetailsTeamDto>> listAllTeams(){
        return ResponseEntity.ok(
                teamService.listTeams()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DetailsTeamDto> editTeam(@PathVariable("id") Long id,
                                                   @RequestBody AddTeamDto teamDto){
        DetailsTeamDto detailsTeamDto = teamService.editTeam(id, teamDto);

        return ResponseEntity.ok(detailsTeamDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable("id") Long id){
        teamService.deleteTeam(id);

        return ResponseEntity.ok("Team successfully deleted!");
    }



}
