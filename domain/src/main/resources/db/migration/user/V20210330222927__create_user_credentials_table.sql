CREATE TABLE user_credentials
(
    email    varchar not null primary key,
    password varchar not null,
    enabled  boolean default true
);

ALTER TABLE clinic
    ADD COLUMN email varchar not null default 'null';
ALTER TABLE clinic
    ADD constraint clinic_email_fkey foreign key (email) references user_credentials (email);

ALTER TABLE doctor
    ADD COLUMN email varchar not null default 'null';
ALTER TABLE doctor
    ADD constraint doctor_email_fkey foreign key (email) references user_credentials (email);

ALTER TABLE patient
    ADD COLUMN email varchar not null default 'null';
ALTER TABLE patient
    ADD constraint patient_email_fkey foreign key (email) references user_credentials (email);