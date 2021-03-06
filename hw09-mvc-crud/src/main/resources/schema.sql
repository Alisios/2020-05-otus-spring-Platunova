DROP TABLE IF EXISTS Authors CASCADE;
DROP TABLE IF EXISTS Genres CASCADE;
DROP TABLE IF EXISTS Books CASCADE;
DROP TABLE IF EXISTS Comments CASCADE;



create table Genres
(
    id   bigint generated by default as identity ,
    type varchar(255),
    primary key (id)
);

create table Authors
(
    id      bigint generated by default as identity,
    name    varchar(255),
    surname varchar(255),
    primary key (id)
);

create table Books
(
    id        bigint generated by default as identity,
    title      varchar(255),
    author_id bigint NOT NULL,
    genre_id  bigint NOT NULL,
    primary key (id)
);

create table Comments
(
    id        bigint generated by default as identity,
    text      varchar(255),
    date      timestamp,
    book_id   bigint NOT NULL,
    primary key (id)
);

alter table if exists Books
    add constraint authorConstraint
        foreign key (author_id)
            references Authors
             ON DELETE CASCADE;

alter table if exists Books
    add constraint genreConstraint
        foreign key (genre_id)
            references Genres
            ON DELETE CASCADE;

alter table if exists Comments
    add constraint bookConstraint
        foreign key (book_id)
            references Books
            ON DELETE CASCADE;

