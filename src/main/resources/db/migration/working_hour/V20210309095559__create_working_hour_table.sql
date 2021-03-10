create table working_hour
(
    id              serial not null primary key,
    time            time   not null,
    day_of_the_week text   not null
);