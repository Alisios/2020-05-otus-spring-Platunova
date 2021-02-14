DROP TABLE IF EXISTS Users_sub CASCADE;
DROP TABLE IF EXISTS Subscriptions CASCADE;

create table sub_user
(
    sub_id  uuid not null,
    user_id uuid not null,
    primary key (sub_id, user_id)
);

create table subscriptions
(
    id        uuid not null,
    ticker    varchar(255),
    typeEvent varchar(255),
    primary key (id)
);

create table users_sub
(
    id           uuid         not null,
    user_real_id varchar(255) not null,
    email        varchar(255),
    telegram     varchar(255),
    primary key (id)
);

alter table if exists sub_user
    add constraint FKrd3t8lewbl9nb3nuj1rhe440b foreign key (user_id) references users_sub

alter table if exists sub_user
    add constraint FKn55lx3cn7qq2cfrl3twi8pjsc foreign key (sub_id) references subscriptions