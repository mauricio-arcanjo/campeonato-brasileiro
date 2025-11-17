package com.mauarcanjo.campeonato_brasileiro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "league_table")
public class LeagueTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Team team;

    @Enumerated(EnumType.STRING)
    private Serie serie;

    private int year;
    private int numberOfMatches;
    private int goalsScored;
    private int goalsSuffered;
    private int goalsBalance;
    private int wins;
    private int draws;
    private int loses;
    private int points;

    @Enumerated(EnumType.STRING)
    private LeagueStatus status;

    public LeagueTable(Team team, int year, Serie serie) {
        this.id = null;
        this.team = team;
        this.serie = serie;
        this.year = year;
        this.numberOfMatches = 0;
        this.goalsScored = 0;
        this.goalsSuffered = 0;
        this.goalsBalance = 0;
        this.wins = 0;
        this.draws = 0;
        this.loses = 0;
        this.points = 0;
        this.status = LeagueStatus.IN_PROGRESS;
    }

    public void setResult(MatchResults result, int goalsScored, int goalsSuffered) {
        this.numberOfMatches++;
        this.goalsScored += goalsScored;
        this.goalsSuffered += goalsSuffered;
        this.goalsBalance = this.goalsScored - this.goalsSuffered;

        switch (result){
            case MatchResults.WIN:
                this.points += 3;
                this.wins++;
                break;
            case MatchResults.DRAW:
                this.points += 1;
                this.draws++;
                break;
            case MatchResults.LOOSE:
                this.loses++;
                break;
        }
    }

    public void complete(){
        this.status = LeagueStatus.COMPLETED;
    }
}
