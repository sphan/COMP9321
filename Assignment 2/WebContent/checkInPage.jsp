<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*, java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hotels</title>
<link href="hotel.css" rel="stylesheet">
</head>
<body>
<div id="header">
<center><h1>Hotels</h1></center>
<div align="right"><c:out value="${staffName}" />
</div>
<hr>
</div>
<div id="content">
	<div id="main-content">
		<p>Room bookings for customer: <c:out value="${customer.firstName}" /> <c:out value="${customer.lastName}" /></p>
		
		<p>Select the rooms for check in.</p>
		<form action="staff" method="POST">
			<table id="result-table">
				<thead>
					<tr id="result-table-header">
						<td>Room Number</td>
						<td>Room Type</td>
						<td>Current Availability</td>
						<td>Check In</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="room" items="${rooms}">
						<tr>
							<td><c:out value="${room.room_number}" /></td>
							<td><c:out value="${room.room_type}" /></td>
							<td><c:out value="${room.availability} "/></td>
							<td><input type="checkbox" name="checkInRooms" value="<c:out value="${room.id}" />" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<input type="submit" name="action" value="Back" />
			<input type="submit" name="action" value="Check In" />
		</form>
	</div>
</div>
</body>
</html>