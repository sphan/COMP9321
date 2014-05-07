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
	<form action="payment" method="post">
		<c:choose>
			<c:when test="${bookingEmpty==false}">
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
						<c:forEach items="${booking.list}" var="bookingSelection">
							<tr>
								<td>${bookingSelection.index}</td>
								<td>${bookingSelection.roomType}</td>
								<td>${bookingSelection.price}</td>
								<td>
								<c:set value="${bookingSelection.roomType=='SINGLE'?'disabled':''}"var="disable"/>
								<input type="checkbox" name="extrabed" ${disable} value="false">
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div>Total: ${totalPrice}</div>
			</c:when>
			<c:otherwise>
			nothing selected
		</c:otherwise>
		</c:choose>

		<div align="center">
			Please enter your information:
			<div>
				Name:<input type="text" name="name"><br> UserName:<input
					type="text" name="username"><br> Password:<input
					type="password" name="name"><br> Email:<input
					type="text" name="email"><br>
			</div>
		</div>
		<input type="submit" value="confirm">
	</form>
</body>
</html>