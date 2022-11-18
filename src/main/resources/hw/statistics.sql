select (select count(*) from "user") as "количество пользователей",
		(select count(*) from "post") as "количество постов",
		(select count(*) from "comment") as "количество комментариев",
		(select count(*) from "like") as "количество лайков";