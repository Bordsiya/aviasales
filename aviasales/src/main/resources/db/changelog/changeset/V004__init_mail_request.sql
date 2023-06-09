create table if not exists mail_request
(
    id         bigserial primary key,
    user_id    bigint       not null REFERENCES users (id),
    status     varchar(255) not null,
    payload    text         not null,
    created_at timestamptz  not null
);

alter table mail_request
    owner to "user";
