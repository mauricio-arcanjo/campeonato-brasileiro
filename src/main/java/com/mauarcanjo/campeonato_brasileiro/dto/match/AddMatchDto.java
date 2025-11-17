package com.mauarcanjo.campeonato_brasileiro.dto.match;

public record AddMatchDto(
        String homeTeamName,
        String visitorTeamName,
        int goalsHomeTeam,
        int goalsVisitorTeam,
        int year
) {
}
