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
	<jsp:include page="header.jsp"></jsp:include>
	<form action="Payment" method="post">
		<c:choose>
			<c:when test="${searchEmpty==false}">
				<table id="result-table" align="center">
					<thead>
						<tr id="result-table-header">
							<td>Room Type</td>
							<td>Price per night</td>
							<td>Count</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${bookingChoices}" var="booking">
							<input type="hidden" name="${booking.roomType}|roomType"
								value="${booking.roomType}">
							<input type="hidden" name="${booking.roomType}|price"
								value="${booking.price}">
							<input type="hidden" name="${booking.roomType}|count"
								value="${booking.count}">
							<tr>
								<td>${booking.roomType}</td>
								<td>${booking.price}</td>
								<td>${booking.count}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:when>
			<c:otherwise>
			nothing selected
		</c:otherwise>
		</c:choose>

		<input type="submit" value="confirm">
	</form>
</body>
</html>