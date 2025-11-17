create table matches(
    id bigint not null auto_increment,
    home_team_id bigint not null,
    visitor_team_id bigint not null,
    goals_home_team int not null,
    goals_visitor_team int not null,
    primary key(id),
    constraint fk_matches_homeTeam_id foreign key(home_team_id) references teams(id),
    constraint fk_matches_visitorTeam_id foreign key(visitor_team_id) references teams(id)
);