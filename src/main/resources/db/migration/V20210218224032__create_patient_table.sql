create table patient
(
    id           serial      not null primary key,
    first_name   varchar(50) not null,
    middle_name  varchar(50) not null,
    last_name    varchar(50) not null,
    email        varchar(50) not null,
    phone_number varchar(20) not null
);