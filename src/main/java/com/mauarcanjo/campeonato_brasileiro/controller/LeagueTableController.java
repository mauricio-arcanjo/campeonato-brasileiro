package com.mauarcanjo.campeonato_brasileiro.controller;

import com.mauarcanjo.campeonato_brasileiro.entity.Serie;
import com.mauarcanjo.campeonato_brasileiro.service.impl.LeagueTableServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/league-table")
public class LeagueTableController {

    private LeagueTableServiceImpl leagueTableService;

    @PostMapping("/{year}")
    public ResponseEntity<String> generateLeagueTable(@PathVariable("year") int year, @RequestParam("serie")Serie serie){

        leagueTableService.fillUpTable(year, serie);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("/{year}/complete")
    public ResponseEntity<String> completeStatusLeagueTable (@PathVariable("year") int year, @RequestParam("serie") Serie serie){

        leagueTableService.completeTable(year, serie);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("/{year}/relegate")
    public ResponseEntity<String> relegateTeams (@PathVariable("year") int year, @RequestParam("serie") Serie serie){

        leagueTableService.relegateLastFourQualified(year, serie);
        return ResponseEntity.ok("Success!");
    }

}
