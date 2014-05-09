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
<div>Logged in as: <c:out value="${loginName}" /></div>
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
					<c:forEach var="occupancy" items="${hotel.value}">
							<tr>
								<td><c:out value="${occupancy.roomType}" /></td>
								<td><c:out value="${occupancy.availability}" /></td>
								<td><c:out value="${occupancy.roomCount}" /></td>
							</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:forEach>
	</div>
</div>
</body>
</html>