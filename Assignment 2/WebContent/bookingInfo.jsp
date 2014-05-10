<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hotels</title>
</head>
<body>
<h1 align="center">Order for ${bookingDetails.customer.firstName} ${bookingDetails.customer.lastName}</h1>

<h2 align="center">Your bookings for ${bookingDetails.startDateString} to ${bookingDetails.endDateString}</h2>

<h2 align="center">at ${bookingDetails.hotel.location}</h2>

<h3 align="center">Your Booking Details:</h3>



</body>
</html>