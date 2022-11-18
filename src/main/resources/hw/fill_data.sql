insert into "user" (name, password) values ('yuri', '1234');
insert into post ("text", user_id) values ('op op', 1);
insert into "comment" ("text", post_id, user_id) values ('comon comon', 1, 1);
insert into "like" (user_id, post_id, comment_id) values (1,1,null);