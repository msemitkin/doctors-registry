create table appointment
(
    id                  serial  not null primary key,
    patient_id          integer not null,
    appointment_hour_id integer not null,
    constraint patient_id_fkey foreign key (patient_id) references patient (id),
    constraint appointment_hour_id_fkey foreign key (appointment_hour_id) references appointment_hour (id)
);