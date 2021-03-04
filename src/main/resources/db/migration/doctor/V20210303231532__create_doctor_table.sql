create table doctor
(
    id                serial      not null primary key,
    first_name        varchar(50) not null,
    last_name         varchar(50) not null,
    specialization_id integer,
    clinic_id         integer,
    price             integer     not null,
    constraint specialization_id_fkey foreign key (specialization_id) references specialization (id),
    constraint clinic_id_fkey foreign key (clinic_id) references clinic (id)
);
