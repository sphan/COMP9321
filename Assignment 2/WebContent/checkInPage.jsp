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
<form action="login" method="POST">
<div align="right"><input type="submit" name="action" value="Logout" /></div>
</form>
<center><h1>Hotels</h1></center>
<div>Logged in as: <c:out value="${loginName}" />
</div>
<hr>
</div>
<div id="content">
	<div id="main-content">
		<c:choose>
			<c:when test="${checkedIn eq false}">
				<p>Room bookings for customer: <c:out value="${customer.firstName}" /> <c:out value="${customer.lastName}" /></p>
				<input type="hidden" name="customerFirstName" value="<c:out value="${customer.firstName}" />" />
				<input type="hidden" name="customerLastName" value="<c:out value="${customer.lastName}" />" />
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
									<td><c:out value="${room.roomNumber}" /></td>
									<td><c:out value="${room.roomType}" /></td>
									<td><c:out value="${room.availability} "/></td>
									<td><input type="checkbox" name="checkInRooms" value="<c:out value="${room.id}" />" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<input type="submit" name="action" value="Back" />
					<input type="submit" value="Check In" />
					<input type="hidden" name="action" value="checkIn" />
				</form>
			</c:when>
			<c:when test="${checkedIn eq true}">
				<p>The following rooms have been checked in:</p>
				<form action="staff" method="POST">
					<table id="result-table">
						<thead>
							<tr id="result-table-header">
								<td>Room Number</td>
								<td>Room Type</td>
								<td>Current Availability</td>
								<td>Check out</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="room" items="${rooms}">
								<tr>
									<td><c:out value="${room.roomNumber}" /></td>
									<td><c:out value="${room.roomType}" /></td>
									<td><c:out value="${room.availability} "/></td>
									<td><input type="checkbox" name="checkOutRooms" value="<c:out value="${room.id}" />" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<input type="submit" name="action" value="Back" />
					<input type="submit" value="Check Out" />
					<input type="hidden" name="action" value="checkOut" />
				</form>
			</c:when>
		</c:choose>
	</div>
</div>
</body>
</html>