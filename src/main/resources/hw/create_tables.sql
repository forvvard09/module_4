create table if not exists "user" (id serial primary key,
					 name varchar(100) not null unique,
					 password varchar(100) not null,
					 created_at timestamp default now());

create table if not exists post (id serial primary key,
								"text" text not null,
								 created_at timestamp default now(),
								 user_id int references "user"(id) not null);

create table if not exists "comment" (id serial primary key,
									  "text" text not null,
								 	  created_at timestamp default now(),
									  user_id int references "user"(id) not null,
									  post_id int references "post"(id) not null);


create table if not exists "like" (id serial primary key,
								   user_id int references "user"(id) not null,
								   post_id int references "post"(id),
								   comment_id int references "comment"(id),
								   unique(user_id, post_id),
								   unique(user_id, comment_id),
								   check (
									   ((coalesce(post_id, 0))::boolean::int + (coalesce(comment_id, 0))::boolean::int) = 1));
