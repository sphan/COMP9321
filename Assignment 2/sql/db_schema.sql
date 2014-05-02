-- Run the following commands to delete all table and re-initialise database.
-- drop table customer_booking;
-- drop table booking;
-- drop table room;
-- drop table customer;
-- drop table owner;
-- drop table staff;
-- drop table hotel;

create table hotel (
	id int not null generated always as identity,
	name varchar(20),
	location varchar(20),
	primary key (id)
);

-- For room_type and availability, I want to make it an enum
-- but don't know how, so currently it is only represent by
-- an integer.
-- For room_type, 0 - 4 represents Single, Double, Queen, Executive, Suite.
-- For availability 0 - 2 represents available, booked, checkedin.
create table room (
	id int not null generated always as identity,
	room_number smallint,
	price float(10),
	discounted_price float(20),
	room_type character(10),
	constraint chk_room_type check
	(room_type='single' or room_type='double' or room_type='queen' or room_type='executive' or room_type='suite'),
	availability character(10),
	constraint chk_availability check
	(availability='available' or availability='booked' or availability='checkedin'),
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