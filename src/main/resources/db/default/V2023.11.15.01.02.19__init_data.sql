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


