-- Run the folloing commands to delete all table and re-initialise database.
-- drop table hotel;
-- drop table room;
-- drop table customer;
-- drop table owner;
-- drop table booking;
-- drop table staff;
-- drop table customer_booking;

create table hotel (
	id int not null generated always as identity,
	name varchar(20),
	location varchar(20),
	primary key (id)
);

create table room (
	id int not null generated always as identity,
	name varchar(20),
	price float(10),
	discounted_price float(20),
	room_type smallint,
	availability smallint,
	hotel int,
	foreign key (hotel) references hotel(id),
	primary key (id)
);

create table customer (
	id int not null generated always as identity,
	name varchar(20),
	username varchar(20),
	password varchar(20),
	primary key (id)
);

create table staff (
	id int not null generated always as identity,
	name varchar(20),
	username varchar(20),
	password varchar(20),
	primary key (id)
);

create table owner (
	id int not null generated always as identity,
	name varchar(20),
	username varchar(20),
	password varchar(20),
	primary key (id)
);

create table booking (
	id int not null generated always as identity,
	start_date date,
	end_date date,
	primary key (id)
);

create table customer_booking (
	booking int not null,
	customer int not null,
	room int not null,
	foreign key (booking) references booking(id),
	foreign key (customer) references customer(id),
	foreign key (room) references room(id),
	primary key (booking, customer, room)
);