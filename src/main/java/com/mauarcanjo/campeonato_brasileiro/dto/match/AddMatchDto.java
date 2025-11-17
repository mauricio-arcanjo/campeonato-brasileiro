package com.mauarcanjo.campeonato_brasileiro.dto.match;

import com.mauarcanjo.campeonato_brasileiro.entity.Serie;

public record AddMatchDto(
        String homeTeamNameOrAbbreviation,
        String visitorTeamNameOrAbbreviation,
        int goalsHomeTeam,
        int goalsVisitorTeam,
        Serie serie,
        int year
) {
}
