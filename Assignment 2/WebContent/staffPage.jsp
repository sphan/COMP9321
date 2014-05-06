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
<div align="right">Hi, <c:out value="${staffName}" />
<input type="hidden" name="staffName" value="<c:out value="${staffName}" />" />
</div>
<hr>
</div>
<div id="content">
	<%@ include file="staffSearchForm.html"%>
	<div id="main-content">
		<c:choose>
			<c:when test="${resultNum eq 0}">
				<p>There currently are no pending bookings.</p>
			</c:when>
			<c:when test="${resultNum gt 0}">
				<p>All Customer Bookings:</p>
				<form action="control" method="POST">
					<table id="result-table">
						<thead>
							<tr id="result-table-header">
								<td>Booking Number</td>
								<td>Customer</td>
								<td>Booking Status</td>
								<td>Select</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="booking" items="${booked}">
								<tr>
									<td><c:out value="${booking.id}" /></td>
									<td><c:out value="${booking.customer.name}" /></td>
									<td>Booked</td>
									<td><input type="radio" name="bookingID" value="<c:out value="${booking.id}" />" />
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div>
						<input type="submit" name="action" value="Select Booking">
					</div>
				</form>
			</c:when>
		</c:choose>
	</div>
</div>
</body>
</html>