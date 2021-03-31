CREATE TABLE user_credentials
(
    email    varchar not null primary key,
    password varchar not null,
    enabled  boolean default true
);

ALTER TABLE clinic
    ADD COLUMN email varchar;
ALTER TABLE clinic
    ADD constraint email_fkey foreign key (email) references user_credentials (email);

ALTER TABLE doctor
    ADD COLUMN email varchar;
ALTER TABLE doctor
    ADD constraint email_fkey foreign key (email) references user_credentials (email);

ALTER TABLE patient
    ADD COLUMN email varchar;
ALTER TABLE patient
    ADD constraint email_fkey foreign key (email) references user_credentials (email);