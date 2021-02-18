create table doctor
(
    id          serial      not null primary key,
    first_name  varchar(50) not null,
    middle_name varchar(50) not null,
    last_name   varchar(50) not null,
    clinic_id   integer     not null,
    price       integer     not null,
    description text,
    constraint clinic_id_fkey foreign key (clinic_id) references clinic (id)
);