create table recommendation
(
    id           bigserial primary key,
    user_id      bigint       not null,
    text         text         not null,
    type         varchar(255) not null,
    created_date date         not null
);

alter table public.recommendation
    owner to "user";
