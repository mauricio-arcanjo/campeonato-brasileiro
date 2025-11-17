package com.mauarcanjo.campeonato_brasileiro.controller;


import com.mauarcanjo.campeonato_brasileiro.dto.match.AddMatchDto;
import com.mauarcanjo.campeonato_brasileiro.dto.match.DetailsMatchDto;
import com.mauarcanjo.campeonato_brasileiro.service.impl.MatchServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/matches")
public class MatchController {

    private MatchServiceImpl matchService;

    @PostMapping
    public ResponseEntity<DetailsMatchDto> addMatch(@RequestBody AddMatchDto addMatchDto){

        DetailsMatchDto detailsMatchDto = matchService.addMatch(addMatchDto);

        return ResponseEntity.ok(detailsMatchDto);

    }

    @PostMapping("/sort-remaining/{year}")
    public ResponseEntity<String> sortAllRemainingMatches(@PathVariable("year") int year){

        matchService.sortAllRemainingMatches(year);

        return ResponseEntity.ok("Success!");
    }

}
