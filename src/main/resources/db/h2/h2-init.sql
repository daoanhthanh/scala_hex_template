create table if not exists items
(
    id    bigint auto_increment,
    name  varchar(255)   not null,
    price numeric(12, 2) not null,
    constraint items_pk
        primary key (id)
);

insert into items (name, price)
values ('pen', 1.00),
       ('pencil', 0.50),
       ('eraser', 0.25),
       ('notebook', 2.00),
       ('paper', 0.3);


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

 /*
insert into admins (identify, account_id, email, name, password, created_at)
values ('f2018601-5761-4d53-93fb-583449936f75', 'Ahihi', 'ahihi@gmail.com', 'Ahihi',
        '123456', now()),
       ('f2018601-5761-4d53-93fb-583449936f76', 'do_ngok', 'y_marutani@septeniventures.co.jp', '丸谷　陽介',
        '654321', now());
*/