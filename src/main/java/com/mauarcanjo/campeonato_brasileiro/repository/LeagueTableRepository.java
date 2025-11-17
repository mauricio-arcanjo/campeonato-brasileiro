package com.mauarcanjo.campeonato_brasileiro.repository;

import com.mauarcanjo.campeonato_brasileiro.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueTableRepository extends JpaRepository<LeagueTable, Long> {

    LeagueTable findByTeam(Team team);
    List<LeagueTable> findAllByYearAndSerie (int year, Serie serie);
    List<LeagueTable> findTop4ByYearAndSerieOrderByPointsAsc(int year, Serie serie);
    long countByYearAndSerieAndStatusNot (int year, Serie serie, LeagueStatus status);
}
