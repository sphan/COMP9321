<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hotels</title>
<link href="hotel.css" rel="stylesheet">
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>

	<div id="content">
		<%@ include file="searchForm.html"%>
		<div id="main-content">
			<p>rooms matching your results:</p>
			<form action="Booking" method="POST">
				<input type="hidden" name="location" value="${location}"> <input
					type="hidden" name="maxPrice" value="${maxPrice}">
				<table id="result-table">
					<thead>
						<tr id="result-table-header">
							<td>Room Type</td>
							<td>Price per night</td>
							<td>Count</td>
							<td>Select</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${roomTypeList}" var="roomType">
							<tr>
								<td>${roomType.roomType}</td>
								<td>${roomType.price}</td>
								<td>${roomType.count}</td>
								<td><input type="number" name=${roomType.roomType
									} value="0" min="0" max=${roomType.count}></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<input align="right" type="submit" value="submit">
			</form>
		</div>
	</div>
	<div id="clearing-div"></div>
</body>
</html>