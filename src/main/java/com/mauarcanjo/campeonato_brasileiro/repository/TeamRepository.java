package com.mauarcanjo.campeonato_brasileiro.repository;

import com.mauarcanjo.campeonato_brasileiro.entity.Serie;
import com.mauarcanjo.campeonato_brasileiro.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByNameOrAbbreviation (String name, String abbreviation);
    boolean existsByName (String name);
    boolean existsByAbbreviation(String abbreviation);
    long countBySerie(Serie serie);
    List<Team> findAllBySerie (Serie serie);
//    Optional<Team> findByNameOrAbbreviation(String name, String abbreviation);
    Optional<Team> findByAbbreviation(String abbreviation);
    Optional<Team> findByName(String name);
}
