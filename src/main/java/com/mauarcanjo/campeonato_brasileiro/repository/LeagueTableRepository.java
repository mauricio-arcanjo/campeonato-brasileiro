package com.mauarcanjo.campeonato_brasileiro.repository;

import com.mauarcanjo.campeonato_brasileiro.entity.LeagueTable;
import com.mauarcanjo.campeonato_brasileiro.entity.Match;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueTableRepository extends JpaRepository<LeagueTable, Long> {

    LeagueTable findByTeam(Team team);

}
