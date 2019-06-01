create table share
(
    id        bigserial
        constraint share_pk primary key,
    sheet     varchar(20) not null,
    selection varchar(50) not null
);

create table share_users
(
    share_id bigserial
        constraint share___fk
            references share,
    user_id bigserial
        constraint users___fk
            references users
);

alter table users alter column email set not null;
