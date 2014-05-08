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
	<%@ include file="ownerSearchForm.html"%>
	<div id="main-content">
		<p>Room Occupancy:</p>
		<c:forEach var="hotel" items="${occupancies}">
			<div><h4><c:out value="${hotel.key}" /></h4></div>
			<table id="result-table">
				<thead>
					<tr id="result-table-header">
						<td>Room Type</td>
						<td>Availability</td>
						<td>Count</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="roomType" items="${hotel.value}">
						<c:forEach var="occupancy" items="${roomType.value}">
							<tr>
								<td><c:out value="${roomType.key}" /></td>
								<td><c:out value="${occupancy.key}" /></td>
								<td><c:out value="${occupancy.value}" /></td>
							</tr>
						</c:forEach>
					</c:forEach>
				</tbody>
			</table>
		</c:forEach>
	</div>
</div>
</body>
</html>