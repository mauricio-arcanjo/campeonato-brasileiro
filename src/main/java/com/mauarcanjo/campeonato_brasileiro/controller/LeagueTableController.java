package com.mauarcanjo.campeonato_brasileiro.controller;

import com.mauarcanjo.campeonato_brasileiro.service.impl.LeagueTableServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/league-table")
public class LeagueTableController {

    private LeagueTableServiceImpl leagueTableService;

    @GetMapping("/{year}")
    public ResponseEntity<String> generateLeagueTable(@PathVariable("year") int year){

        leagueTableService.fillUpTable(year);
        return ResponseEntity.ok("Success!");
    }

}
