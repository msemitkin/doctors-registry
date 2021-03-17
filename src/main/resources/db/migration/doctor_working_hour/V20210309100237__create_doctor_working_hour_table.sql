create table doctor_working_hour
(
    id              serial not null primary key,
    doctor_id       integer,
    working_hour_id integer,
    constraint doctor_id_fkey foreign key (doctor_id) references doctor (id) ON DELETE CASCADE,
    constraint working_hour_id_fkey foreign key (working_hour_id) references doctor_working_hour (id)
);