create table admins
(
    id         int auto_increment
        primary key,
    identify   varchar(255) not null,
    account_id varchar(255) not null,
    email      varchar(255) not null,
    name       varchar(255) not null,
    password   text         not null,
    created_at datetime     not null,
    constraint account_id
        unique (account_id),
    constraint email
        unique (email),
    constraint identify
        unique (identify)
);

create index identify_2
    on admins (identify);


