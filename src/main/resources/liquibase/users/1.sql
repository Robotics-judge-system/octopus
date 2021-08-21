--liquibase formatted sql

--changeset anarcom:1 created users (for judges) table
CREATE TABLE users
(
    id             bigserial primary key,
    name           varchar(50)                         not null,
    email          varchar(20)                         not null,
    created_at     timestamp default CURRENT_TIMESTAMP not null,
    updated_at     timestamp default CURRENT_TIMESTAMP not null,
    status         varchar(10)                         not null,
    password_hash  varchar(255)                        not null,
    last_online_at timestamp
);