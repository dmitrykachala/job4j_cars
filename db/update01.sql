CREATE TABLE users (
                                     id SERIAL PRIMARY KEY,
                                     name TEXT,
                                     email TEXT,
                                     password TEXT
);
create table engine(
    id serial primary key
);

create table driver(
    id serial primary key
);

create table car(
                    id serial primary key,
                    name text,
                    body text,
                    engine_id int not null references engine(id)
);

create table history_owner(
                              id serial primary key,
                              driver_id int not null references driver(id),
                              car_id int not null references car(id)
);

create table ad(
                    id serial primary key,
                    description text,
                    created timestamp,
                    sold boolean,
                    picture_link text,
                    car_id int not null references car(id),
                    user_id int not null references users(id)
);