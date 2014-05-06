insert into HOTEL values (default, 'Ibis', 'Sydney');
insert into HOTEL values (default, 'Mecure', 'Melbourne');
insert into HOTEL values (default, 'Ibis', 'Brisbane');
insert into HOTEL values (default, 'Ibis', 'Adelaide');
insert into HOTEL values (default, 'Ibis', 'Hobart');

insert into ROOM_TYPE values (default, 'SINGLE', 7000, 0);
insert into ROOM_TYPE values (default, 'DOUBLE', 12000, 0);
insert into ROOM_TYPE values (default, 'QUEEN', 12000, 0);
insert into ROOM_TYPE values (default, 'EXECUTIVE', 18000, 0);
insert into ROOM_TYPE values (default, 'SUITE', 30000, 0);

-- Sydney hotel
insert into ROOM values (default, 1,101, 'booked', 1);
insert into ROOM values (default, 2,102, 'booked', 1);
insert into ROOM values (default, 3,103, 'booked', 1);
insert into ROOM values (default, 4,104, 'booked', 1);
insert into ROOM values (default, 5,105, 'booked', 1);
insert into ROOM values (default, 1,106, 'booked', 1);
insert into ROOM values (default, 2,107, 'booked', 1);
insert into ROOM values (default, 3,108, 'booked', 1);
insert into ROOM values (default, 4,109, 'booked', 1);
insert into ROOM values (default, 5,110, 'booked', 1);

-- Melbourne hotel
insert into ROOM values (default, 1,101, 'booked', 2);
insert into ROOM values (default, 2,102, 'booked', 2);
insert into ROOM values (default, 3,103, 'booked', 2);
insert into ROOM values (default, 4,104, 'booked', 2);
insert into ROOM values (default, 5,105, 'booked', 2);

-- Brisbane hotel
insert into ROOM values (default, 1,101, 'booked', 3);
insert into ROOM values (default, 2,102, 'booked', 3);
insert into ROOM values (default, 3,103, 'booked', 3);
insert into ROOM values (default, 4,104, 'booked', 3);
insert into ROOM values (default, 5,105, 'booked', 3);

-- Adelaide hotel
insert into ROOM values (default, 1,101, 'booked', 4);
insert into ROOM values (default, 2,102, 'booked', 4);
insert into ROOM values (default, 3,103, 'booked', 4);
insert into ROOM values (default, 4,104, 'booked', 4);
insert into ROOM values (default, 5,105, 'booked', 4);

-- Hobart hotel
insert into ROOM values (default, 1,101, 'booked', 5);
insert into ROOM values (default, 2,102, 'booked', 5);
insert into ROOM values (default, 3,103, 'booked', 5);
insert into ROOM values (default, 4,104, 'booked', 5);
insert into ROOM values (default, 5,105, 'booked', 5);

insert into CUSTOMER values (default, 'Andrew', 'Andrew', 'password');
insert into CUSTOMER values (default, 'Bill', 'Bill', 'password');
insert into CUSTOMER values (default, 'Claire', 'Claire', 'password');
insert into CUSTOMER values (default, 'Daniel', 'Daniel', 'password');
insert into CUSTOMER values (default, 'Ethan', 'Ethan', 'password');
insert into CUSTOMER values (default, 'Frank', 'Frank', 'password');
insert into CUSTOMER values (default, 'Gary', 'Gary', 'password');
insert into CUSTOMER values (default, 'Harold', 'Harold', 'password');

insert into CUSTOMER_BOOKING values (default, 2, '2014-05-09', '2014-05-11');
insert into CUSTOMER_BOOKING values (default, 3, '2014-05-04', '2014-05-04');
insert into CUSTOMER_BOOKING values (default, 4, '2014-05-03', '2014-05-04');
insert into CUSTOMER_BOOKING values (default, 5, '2014-05-03', '2014-05-04');

insert into STAFF values (default, 'Sandy', 'Sandy', 'password', 'manager');
insert into STAFF values (default, 'Jason', 'Jason', 'password', 'manager');
insert into STAFF values (default, 'Kate', 'Kate', 'password', 'owner');

--select rt.room_type, rt.price, count(rt.room_type) as count from room r join room_type rt on (r.room_type_id=rt.id) join hotel h on (h.id=r.hotel_id) where h.location='Sydney' and rt.price <= 13000 group by rt.room_type, rt.price