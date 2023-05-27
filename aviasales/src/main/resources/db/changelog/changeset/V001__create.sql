create table public.airlines
(
    id   bigint primary key     not null,
    name character varying(255) not null
);

create table public.airports
(
    id      bigint primary key     not null,
    code    character varying(255) not null,
    name    character varying(255) not null,
    city    character varying(255) not null,
    country character varying(255) not null,
    state   character varying(255)
);

create table public.reservations
(
    id           bigint primary key          not null,
    email        character varying(255)      not null,
    phone_number character varying(255)      not null,
    code         character varying(255)      not null,
    time         timestamp without time zone not null
);

create table public.tariffs
(
    id                            bigint primary key     not null,
    amount_of_baggage             bigint,
    amount_of_hand_baggage        bigint                 not null,
    depth_per_hand_baggage_in_cm  bigint                 not null,
    has_baggage                   boolean                not null,
    has_exchange                  boolean                not null,
    has_refund                    boolean                not null,
    height_per_hand_baggage_in_cm bigint                 not null,
    price_for_adults              bigint                 not null,
    price_for_kids                bigint                 not null,
    sum_of_baggage_sides_in_cm    bigint,
    tariff_name                   character varying(255) not null,
    tariff_type                   character varying(255) not null,
    weight_per_baggage_in_kg      bigint,
    weight_per_hand_baggage_in_kg bigint                 not null,
    width_per_hand_baggage_in_cm  bigint                 not null,
    airline_id                    bigint                 not null,
    foreign key (airline_id) references public.airlines (id)
        match simple on update no action on delete no action
);


create table public.aircrafts
(
    id              bigint primary key     not null,
    manufacturer    character varying(255) not null,
    model           character varying(255) not null,
    number_of_seats integer                not null,
    airline_id      bigint                 not null,
    foreign key (airline_id) references public.airlines (id)
        match simple on update no action on delete no action
);

create table public.flights
(
    id                       bigint primary key     not null,
    arrival_date             date                   not null,
    arrival_time             time without time zone not null,
    default_price_for_adults bigint                 not null,
    default_price_for_kids   bigint                 not null,
    departure_date           date                   not null,
    departure_time           time without time zone not null,
    aircraft_id              bigint                 not null,
    arrival_airport_id       bigint                 not null,
    departure_airport_id     bigint                 not null,
    foreign key (departure_airport_id) references public.airports (id)
        match simple on update no action on delete no action,
    foreign key (aircraft_id) references public.aircrafts (id)
        match simple on update no action on delete no action,
    foreign key (arrival_airport_id) references public.airports (id)
        match simple on update no action on delete no action
);

create table public.passengers
(
    id                       bigint primary key     not null,
    citizenship              character varying(255) not null,
    document_number          character varying(255) not null,
    document_type            character varying(255) not null,
    expiration_date          date,
    first_name               character varying(255) not null,
    gender                   character varying(255) not null,
    has_hearing_difficulties boolean                not null,
    has_vision_difficulties  boolean                not null,
    is_kid                   boolean                not null,
    last_name                character varying(255) not null,
    patronymic               character varying(255) not null,
    required_wheelchair      boolean                not null,
    flight_id                bigint                 not null,
    reservation_id           bigint                 not null,
    tariff_id                bigint                 not null,
    foreign key (flight_id) references public.flights (id)
        match simple on update no action on delete no action,
    foreign key (tariff_id) references public.tariffs (id)
        match simple on update no action on delete no action,
    foreign key (reservation_id) references public.reservations (id)
        match simple on update no action on delete no action
);
