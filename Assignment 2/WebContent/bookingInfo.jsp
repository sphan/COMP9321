<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="searchDetails"
	class="edu.unsw.comp9321.bean.SearchDetailsBean" scope="session" />
<jsp:useBean id="booking" class="edu.unsw.comp9321.bean.BookingListBean"
	scope="session" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hotels</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>

	<%@ include file="searchForm.jsp"%>
	<div id="content">
		<h1 align="center">Order for ${bookingDetails.customer.firstName}
			${bookingDetails.customer.lastName}</h1>

		<h2 align="center">Your bookings for
			${bookingDetails.startDateString} to ${bookingDetails.endDateString}</h2>

		<h2 align="center">at ${bookingDetails.hotel.location}</h2>

		<h3 align="center">Your Booking Details:</h3>
		<table id="result-table" align="center">
			<thead>
				<tr id="result-table-header">
					<td>Room</td>
					<td>Room Type</td>
					<td>Price per night</td>
					<td>Extra bed?</td>
				</tr>
			</thead>
			<tbody>
				<%
					int i = 0;
				%>
				<c:forEach items="${bookingDetails.roomSchedules}"
					var="roomSchedule">
					<tr>
						<td>
							<%
								out.println(i++);
							%>
						</td>
						<td>${roomSchedule.roomType}</td>
						<td>price</td>
						<c:set value="${roomSchedule.extraBed ?'checked':''}" var="check" />
						<td><input type="checkbox"disabled ${check}></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>