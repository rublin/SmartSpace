create table users
(
    id bigserial
        constraint users_pk
            primary key,
    email varchar(64)
);

create unique index users_email_uindex
    on users (email);

