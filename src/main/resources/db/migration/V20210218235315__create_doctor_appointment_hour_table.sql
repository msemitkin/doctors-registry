create table doctor_appointment_hour
(
    id               serial  not null primary key,
    doctor_id        integer not null,
    appointment_date date    not null,
    constraint doctor_id_fkey foreign key (doctor_id) references doctor (id)
);