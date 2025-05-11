-- liquibase formatted sql

-- changeset alex turaev:1
-- drop table if exists items;
-- drop table if exists users;

-- товары
create table if not exists items(
    id bigserial primary key,
    title varchar(255) not null,
    description text,
    price numeric(10, 2),
    quantity int
    );

-- картинки товаров
create table if not exists item_pics(
    item_id bigint,
    image_type varchar(50),
    image bytea,

    foreign key (item_id) references items(id) on delete cascade
    );

-- корзина
create table if not exists cart(
    cart_id bigint,
    item_id bigint,
    count_item int,

    foreign key (item_id) references items(id) on delete cascade,
    constraint cart_item unique(cart_id, item_id)
    );

-- заказы
create table if not exists orders(
    order_id bigint,
    item_id bigint,
    count_item int,
    price numeric(10, 2),

    foreign key (item_id) references items(id),
    constraint order_item unique(order_id, item_id)
    );

-- changeset alex turaev:2
CREATE SEQUENCE order_sequence;