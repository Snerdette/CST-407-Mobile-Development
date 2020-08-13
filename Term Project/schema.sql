CREATE TABLE ToDo (
	id integer PRIMARY KEY AUTOINCREMENT,
	createdAt datetime,
	name varchar
);

CREATE TABLE ToDoListItem (
	id integer PRIMARY KEY AUTOINCREMENT,
	createdAt datetime,
	toDoId integer,
	itemName varchar,
	isComplete integer
);