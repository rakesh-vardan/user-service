create table users.user
(
    id              int8            primary key not null,
    user_name       varchar(80)                 not null,
    role            varchar(10)                 not null,
    email           varchar(30),
    phone_no        varchar(30)
);

create index idx1 on users.user (
    id
);

create sequence user_id_seq
    increment 1
    minvalue 1
    maxvalue 9223372036854775807
    start 1
    cache 1
    owned by users.user.id;

alter table users.user
    alter column id set default nextval('user_id_seq');

