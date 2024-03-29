CREATE TABLE authority
(
    id             serial  not null primary key,
    email          varchar not null,
    authority_name varchar not null,
    constraint authority_email_fkey foreign key (email) references user_credentials (email)
);