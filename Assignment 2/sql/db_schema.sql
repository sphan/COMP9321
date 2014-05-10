-- Run the following commands to delete all table and re-initialise database.
drop table room_schedule;
drop table booking_unique;
drop table customer_booking;
drop table room;
drop table customer;
drop table staff;
drop table discount;
drop table room_type;
drop table hotel;

--################################################################################
--hotel related sql

create table hotel (
	id int not null generated always as identity,
	name varchar(20) not null,
	location varchar(20) not null,
	primary key (id)
);

--################################################################################
--room_type related sql

create table room_type (
	id int not null generated always as identity,
	room_type varchar(10) not null unique,
	constraint chk_room_type check
	(room_type='SINGLE' or room_type='DOUBLE' or room_type='QUEEN' or room_type='EXECUTIVE' or room_type='SUITE'),
	price int not null,
	constraint chk_price check
	(price>=0),
	primary key (id)
);

create table discount (
	id int not null generated always as identity,
	room_type_id int not null,
	start_date date not null,
	end_date date not null,
	discounted_price int not null,
	constraint chk_discounted_price check
	(discounted_price>=0),
	hotel_id int not null,
	foreign key (room_type_id) references room_type(id),
	foreign key (hotel_id) references hotel(id),
	primary key (id)
);

--################################################################################
--room related sql

create table room (
	id int not null generated always as identity,
	room_type_id int not null,
	room_number smallint not null,
	availability varchar(10) not null,
	constraint chk_availability check
	(availability='available' or availability='booked' or availability='checkedin'),
	hotel_id int not null,
	foreign key (room_type_id) references room_type(id),
	foreign key (hotel_id) references hotel(id),
	primary key (id)
);

--################################################################################
--customer related sql

create table customer (
	id int not null generated always as identity,
	first_name varchar(20) not null,
	last_name varchar(20) not null,
	primary key (id)
);

--################################################################################
--booking related sql
--create a booking id to link room schedules onto
create table customer_booking (
	id int not null generated always as identity,
	customer_id int not null,
	start_date date not null,
	end_date date not null,
	hotel_id int not null,
	primary key (id),
	foreign key (customer_id) references customer(id),
	constraint chk_date_range check
	(start_date <= end_date),
	foreign key (hotel_id) references hotel(id)
	--additional constraint added in DAO, check date ranges do not overlap
	--for any particular customer
);

--link between room and booking
create table room_schedule (
	id int not null generated always as identity,
	room_id int,
	customer_booking_id int not null,
	room_type_id int not null,
	extra_bed int not null,
	primary key (id),
	foreign key (room_id) references room(id),
	foreign key (customer_booking_id) references customer_booking(id),
	foreign key (room_type_id) references room_type(id),
	constraint chk_boolean_extra_bed check
	(extra_bed=0 or extra_bed=1)
);

--################################################################################
--staff related sql

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

create table booking_unique (
	id int not null generated always as identity,
	code varchar(30) not null unique,
	customer_booking_id int not null unique,
	primary key(id),
	foreign key (customer_booking_id) references customer_booking(id)
);
insert into customer_booking values (default, 1, '2014-05-09','2014-05-11')
select * from customer_booking;
select * from customer;
insert into room_schedule values(default, null, 1, 1)
select * from room_schedule rs join customer_booking cb on (rs.customer_booking_id=cb.id) join hotel h on (cb.hotel_id=h.id)

select * from booking_unique;

(select rt1.room_type, rt1.price, count(rt1.room_type) as count from room_type rt1 join room r on (r.room_type_id=rt1.id) join hotel h on (h.id=r.hotel_id) where h.id=1 group by rt1.room_type, rt1.price)
(select rt2.room_type, rt2.price, count(rt2.room_type) as count from room_schedule rs join room_type rt2 on (rs.room_type_id=rt2.id) join customer_booking cb on (cb.id=rs.customer_booking_id) where (cb.hotel_id=1) and ((cb.start_date between'2014-04-01' and '2016-04-02') or (cb.end_date between'2014-04-01' and '2016-06-02')) group by rt2.room_type, rt2.price)


select rt.room_type from room r join hotel h on (r.hotel_id=h.id) join room_type rt on (rt.id=r.room_type_id) where h.location='Sydney' and rt.room_type='SINGLE' and r.id not in 
(select rt.room_type from room_schedule rs join customer_booking cb on (rs.customer_booking_id=cb.id) join room r on (r.id=rs.room_id) join room_type rt on (rt.id=r.room_type_id) where (cb.start_date between '2014-05-01' and '2014-05-10') or (cb.end_date between '2014-05-01' and '2014-05-10')) 