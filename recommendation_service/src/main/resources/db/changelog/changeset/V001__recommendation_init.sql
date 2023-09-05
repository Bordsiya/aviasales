create table recommendation
(
    id           bigserial primary key,
    user_id      bigint       not null,
    text         text         not null,
    type         varchar(255) not null,
    created_date date         not null
);

create table city_experience
(
    id          bigserial       primary key,
    user_id     bigint          not null,
    city        varchar(255)    not null,
    scrobbles   bigint          not null
);

alter table public.recommendation
    owner to "user";
