package com.mauarcanjo.campeonato_brasileiro.dto.match;

import com.mauarcanjo.campeonato_brasileiro.entity.Team;

public record DetailsMatchDto(
        Long id,
        Team homeTeam,
        Team visitorTeam,
        int goalsHomeTeam,
        int goalsVisitorTeam,
        int year
) {
}
