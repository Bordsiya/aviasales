create table if not exists public.airlines
(
    id   bigserial
        primary key,
    name varchar(255) not null
);

alter table public.airlines
    owner to "user";

create table if not exists public.aircrafts
(
    id              bigserial
        primary key,
    manufacturer    varchar(255) not null,
    model           varchar(255) not null,
    number_of_seats integer      not null,
    airline_id      bigint       not null
        constraint fklrgmua8va47so5ldeqh752s9r
            references public.airlines
);

alter table public.aircrafts
    owner to "user";

create table if not exists public.airports
(
    id      bigserial
        primary key,
    code    varchar(255) not null,
    name    varchar(255) not null,
    city    varchar(255) not null,
    country varchar(255) not null,
    state   varchar(255)
);

alter table public.airports
    owner to "user";

create table if not exists public.flights
(
    id                       bigserial
        primary key,
    arrival_date             date   not null,
    arrival_time             time   not null,
    default_price_for_adults bigint not null,
    default_price_for_kids   bigint not null,
    departure_date           date   not null,
    departure_time           time   not null,
    aircraft_id              bigint not null
        constraint fko9jvf5gvgbrhgl3nh2obanw6u
            references public.aircrafts,
    arrival_airport_id       bigint not null
        constraint fkr90ujcvdphv3co3ry7aiel6l4
            references public.airports,
    departure_airport_id     bigint not null
        constraint fk27lt4nklvbrwsw7x32dw0d05q
            references public.airports
);

alter table public.flights
    owner to "user";

create table if not exists public.reservations
(
    id           bigserial
        primary key,
    email        varchar(255) not null,
    phone_number varchar(255) not null,
    code         varchar(255) not null,
    time         timestamp    not null
);

alter table public.reservations
    owner to "user";

create table if not exists public.tariffs
(
    id                            bigserial
        primary key,
    amount_of_baggage             bigint,
    amount_of_hand_baggage        bigint       not null,
    depth_per_hand_baggage_in_cm  bigint       not null,
    has_baggage                   boolean      not null,
    has_exchange                  boolean      not null,
    has_refund                    boolean      not null,
    height_per_hand_baggage_in_cm bigint       not null,
    price_for_adults              bigint       not null,
    price_for_kids                bigint       not null,
    sum_of_baggage_sides_in_cm    bigint,
    tariff_name                   varchar(255) not null,
    tariff_type                   varchar(255) not null,
    weight_per_baggage_in_kg      bigint,
    weight_per_hand_baggage_in_kg bigint       not null,
    width_per_hand_baggage_in_cm  bigint       not null,
    airline_id                    bigint       not null
        constraint fklr7q6b2wl5td5ynue8mejcvnv
            references public.airlines
);

alter table public.tariffs
    owner to "user";

create table if not exists public.passengers
(
    id                       bigserial
        primary key,
    citizenship              varchar(255) not null,
    document_number          varchar(255) not null,
    document_type            varchar(255) not null,
    expiration_date          date,
    first_name               varchar(255) not null,
    gender                   varchar(255) not null,
    has_hearing_difficulties boolean      not null,
    has_vision_difficulties  boolean      not null,
    is_kid                   boolean      not null,
    last_name                varchar(255) not null,
    patronymic               varchar(255) not null,
    required_wheelchair      boolean      not null,
    flight_id                bigint       not null
        constraint fkb8phbgdfxqid13uav0cawqyl5
            references public.flights,
    reservation_id           bigint       not null
        constraint fkqjopt2ubnd7nwio9drmeqbcie
            references public.reservations,
    tariff_id                bigint       not null
        constraint fkhpws42yid9h2usr9h3vy7n4id
            references public.tariffs
);

alter table public.passengers
    owner to "user";

create table if not exists public.users
(
    id       bigserial
        primary key,
    email    varchar(255) not null,
    password varchar(255),
    role     varchar(255)
);

alter table public.users
    owner to "user";

create table if not exists public.applications
(
    id                  bigserial       primary key,
    user_id             bigserial       not null
            references public.users,
    application_type    varchar(255)    not null,
    payload             text            not null,
    application_status  varchar(255)    not null,
    publish_date        date            not null,
    is_archived         boolean         not null
);

alter table public.applications
    owner to "user";
