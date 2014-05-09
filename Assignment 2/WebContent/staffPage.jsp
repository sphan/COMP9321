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
			<c:when test="${(bookedNum eq 0) && (checkedinNum eq 0)}">
				<p>There currently are no pending bookings.</p>
			</c:when>
			<c:when test="${(bookedNum ne 0) || (checkedinNum ne 0)}">
				<form action="staff" method="POST">
					<c:if test="${bookedNum ne 0}">
						<p>All pending customer bookings:</p>
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
								<c:forEach var="booking" items="${results['BOOKED']}">
									<tr>
										<td><c:out value="${booking.id}" /></td>
										<td><c:out value="${booking.customer.firstName}" /> <c:out value="${booking.customer.lastName}" /></td>
										<td>BOOKED</td>
										<td><input type="hidden" name="bookingStatus" value="BOOKED" />
										<input type="radio" name="bookingID" value="<c:out value="${booking.id}" />" />
										
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>
					<c:if test="${checkedinNum ne 0}">
						<p>All checked in bookings:</p>
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
								<c:forEach var="booking" items="${results['CHECKEDIN']}">
									<tr>
										<td><c:out value="${booking.id}" /></td>
										<td><c:out value="${booking.customer.firstName}" /> <c:out value="${booking.customer.lastName}" /></td>
										<td>CHECKED IN</td>
										<td><input type="hidden" name="bookingStatus" value="CHECKEDIN" />
										<input type="radio" name="bookingID" value="<c:out value="${booking.id}" />" />
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>
					<div>
						<input type="submit" name="action" value="Select Booking">
						<input type="hidden" name="action" value="selectBooking">
					</div>
				</form>
			</c:when>
		</c:choose>
	</div>
</div>
</body>
</html>