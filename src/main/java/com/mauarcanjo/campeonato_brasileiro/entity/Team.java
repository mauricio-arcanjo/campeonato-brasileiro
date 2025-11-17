package com.mauarcanjo.campeonato_brasileiro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String abbreviation;

    private String state;

    @Enumerated(EnumType.STRING)
    private Serie serie;

    public void relegate(){
        this.serie = Serie.SERIE_B;
    }

    public void promote(){
        this.serie = Serie.SERIE_A;
    }

}
