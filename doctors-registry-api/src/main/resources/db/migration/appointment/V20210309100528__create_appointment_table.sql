create table appointment
(
    id                     serial  not null primary key,
    patient_id             integer not null,
    doctor_working_hour_id integer not null,
    date                   date    not null,
    constraint patient_id_fkey foreign key (patient_id) references patient (id) ON DELETE CASCADE,
    constraint doctor_working_hour_id_fkey foreign key (doctor_working_hour_id) references doctor_working_hour (id) ON DELETE CASCADE
);