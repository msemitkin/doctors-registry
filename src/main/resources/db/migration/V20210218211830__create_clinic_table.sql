create table clinic
(
    id          serial not null primary key,
    name        text   not null,
    address     text   not null,
    description text
);