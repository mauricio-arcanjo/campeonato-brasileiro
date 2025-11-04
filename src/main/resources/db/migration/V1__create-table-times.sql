create table teams(
    id bigint not null auto_increment,
    name varchar(100) not null unique,
    abbreviation varchar(100) not null unique,
    state varchar(2) not null,
    primary key(id)
);