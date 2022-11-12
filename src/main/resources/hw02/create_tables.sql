create table if not exists users (id serial primary key, name varchar(200) not null unique, pasword varchar(15) not null, created_at timestamp);
create table if not exists posts (id serial primary key, text text not null, created_at timestamp, user_id int not null references users(id));
create table if not exists comments (id serial primary key, text text, post_id int not null references posts(id), user_id int not null references users(id), created_at timestamp);
create table if not exists likes (id serial primary key, user_id int not null references users(id), post_id int references posts(id), comment_id int references comments(id));






