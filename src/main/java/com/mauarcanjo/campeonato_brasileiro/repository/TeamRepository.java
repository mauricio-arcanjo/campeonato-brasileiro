package com.mauarcanjo.campeonato_brasileiro.repository;

import com.mauarcanjo.campeonato_brasileiro.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByNameOrAbbreviation (String name, String abbreviation);
    boolean existsByName (String name);
    boolean existsByAbbreviation(String abbreviation);
    Optional<Team> findByNameOrAbbreviation(String name, String abbreviation);
}
