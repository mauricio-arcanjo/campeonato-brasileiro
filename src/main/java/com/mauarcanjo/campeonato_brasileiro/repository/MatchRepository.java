package com.mauarcanjo.campeonato_brasileiro.repository;

import com.mauarcanjo.campeonato_brasileiro.entity.Match;
import com.mauarcanjo.campeonato_brasileiro.entity.Serie;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

//    boolean existsByHomeTeamAndVisitorTeam(Team homeTeam, Team visitorTeam);
    boolean existsByHomeTeamAndVisitorTeamAndYearAndSerie(Team homeTeam, Team visitorTeam, int year, Serie serie);
    List<Match> findAllByYear(int year);
    long countByYear(int year);

}
