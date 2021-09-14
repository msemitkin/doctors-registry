create table doctor_working_hour
(
    id              serial  not null primary key,
    doctor_id       integer not null,
    time            time    not null,
    day_of_the_week integer not null,
    constraint doctor_id_fkey foreign key (doctor_id) references doctor (id) ON DELETE CASCADE
);