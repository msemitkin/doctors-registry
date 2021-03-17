create table doctor_working_hour
(
    id              serial not null primary key,
    doctor_id       integer,
    time            time,
    day_of_the_week integer,
    constraint doctor_id_fkey foreign key (doctor_id) references doctor (id) ON DELETE CASCADE
);