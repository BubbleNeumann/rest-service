create table if not exists Games(
    id bigint auto_increment primary key,
    title varchar(50) not null,
    release_date date not null
);
