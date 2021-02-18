create table doctor_specialization
(
    id                serial  not null primary key,
    doctor_id         integer not null,
    specialization_id integer not null,
    constraint doctor_id_fkey foreign key (doctor_id) references doctor (id),
    constraint specialization_id foreign key (specialization_id) references specialization (id)
);