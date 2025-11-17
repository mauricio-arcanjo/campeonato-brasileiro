create table league_table(
    id bigint not null auto_increment,
    team_id bigint not null,
    number_of_matches int,
    goals_scored int,
    goals_suffered int,
    goals_balance int,
    wins int,
    draws int,
    loses int,
    points int,
    primary key(id),
    constraint fk_matches_team_id foreign key(team_id) references teams(id)
);