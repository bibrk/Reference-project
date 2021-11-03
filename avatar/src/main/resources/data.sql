create table c_code_store_type (
	id integer primary key,
	text varchar(255)
);

create table c_code_store_item (
	id integer primary key,
	type_id integer,
	text varchar(255)
);

insert into c_code_store_type(id, text) values(1, 'Status');
insert into c_code_store_type(id, text) values(2, 'Image types');

insert into c_code_store_item(id, type_id, text) values (1001, 1, 'Active');
insert into c_code_store_item(id, type_id, text) values (1002, 1, 'Deleted');
insert into c_code_store_item(id, type_id, text) values (2001, 2, 'Backround');
insert into c_code_store_item(id, type_id, text) values (2002, 2, 'Head');
insert into c_code_store_item(id, type_id, text) values (2003, 2, 'Body');
insert into c_code_store_item(id, type_id, text) values (2004, 2, 'Leg');

create table c_file_descriptor (
	id serial primary key,
	url varchar(255),
	file_name varchar(255),
	type_id integer
);