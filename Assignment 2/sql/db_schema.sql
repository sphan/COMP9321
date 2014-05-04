-- Run the following commands to delete all table and re-initialise database.
drop table room_schedule;
drop table customer_booking;
drop table room;
drop table customer;
drop table staff;
drop table hotel;

create table hotel (
	id int not null generated always as identity,
	name varchar(20) not null,
	location varchar(20) not null,
	primary key (id)
);

create table room (
	id int not null generated always as identity,
	room_number smallint not null,
	price float(10) not null,
	constraint chk_price check
	(price>=0),
	discounted_price float(20) not null,
	constraint chk_discounted_price check
	(discounted_price>=0),
	room_type varchar(10) not null,
	constraint chk_room_type check
	(room_type='single' or room_type='double' or room_type='queen' or room_type='executive' or room_type='suite'),
	availability varchar(10) not null,
	constraint chk_availability check
	(availability='available' or availability='booked' or availability='checkedin'),
	hotel int not null,
	foreign key (hotel) references hotel(id),
	primary key (id)
);

create table customer (
	id int not null generated always as identity,
	name varchar(20) not null,
	username varchar(20) not null unique,
	password varchar(20) not null,
	primary key (id)
);

--create a booking id to link room schedules onto
create table customer_booking (
	id int not null generated always as identity,
	customer_id int not null,
	start_date date not null,
	end_date date not null,
	primary key (id),
	foreign key (customer_id) references customer(id),
	constraint chk_date_range check
	(start_date <= end_date)
	--additional constraint added in DAO, check date ranges do not overlap
	--for any particular customer
);

--link between room and booking
create table room_schedule (
	id int not null generated always as identity,
	room_id int not null,
	customer_booking_id int not null,
	primary key (id),
	foreign key (room_id) references room(id),
	foreign key (customer_booking_id) references customer_booking(id)
);

create table staff (
	id int not null generated always as identity,
	name varchar(20) not null,
	username varchar(20) not null unique,
	password varchar(20) not null,
	staff_type varchar(10) not null,
	constraint chk_staff_type check
	(staff_type='owner' or staff_type='manager'),
	primary key (id)
);
