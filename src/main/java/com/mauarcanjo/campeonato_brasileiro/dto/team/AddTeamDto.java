package com.mauarcanjo.campeonato_brasileiro.dto.team;

import com.mauarcanjo.campeonato_brasileiro.entity.Serie;

public record AddTeamDto(
        String name,
        String abbreviation,
        String state,
        Serie serie
){

}
