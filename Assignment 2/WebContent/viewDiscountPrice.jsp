<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*, java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	<%@ include file="discountSearchForm.html"%>
	<div id="main-content">
		<form action="owner" method="POST">
			<input type="submit" name="action" value="Back to Main">
		</form>
		<p>Room prices:</p>
		<form action="owner" method="POST">
			<c:forEach var="hotel" items="${roomPrices}">
				<div><h4><c:out value="${hotel.key}" /></h4></div>
				<input type="hidden" name="location[]" value="<c:out value="${hotel.key}" />" />
				<table id="result-table">
					<thead>
						<tr id="result-table-header">
							<td>Room Type</td>
							<td>Original Price</td>
							<td>Discounted Price</td>
							<td>Start Date</td>
							<td>End Date</td>
							<td>Select</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prices" items="${hotel.value}">
								<tr>
									<td><c:out value="${prices.roomType}" /></td>
									<td><c:out value="${prices.currentPrice}" /></td>
									<td> <!-- discounted price -->
									<c:if test="${prices.discountPrice eq 0}">
										<c:out value="N/A" />
									</c:if>
									<c:if test="${prices.discountPrice ne 0}">
										<c:out value="${prices.discountPrice}" />
									</c:if>
									</td>
									<td> <!-- discount start date -->
									<c:if test="${prices.discountStartDate eq ''}">
										<c:out value="N/A" />
									</c:if>
									<c:if test="${prices.discountStartDate ne ''}">
										<c:out value="${prices.discountStartDate}" />
									</c:if>
									</td>
									<td> <!-- discount end date -->
									<c:if test="${prices.discountEndDate eq ''}">
										<c:out value="N/A" />
									</c:if>
									<c:if test="${prices.discountEndDate ne ''}">
										<c:out value="${prices.discountEndDate}" />
									</c:if>
									</td>
									<td>
									<c:if test="${prices.discountPrice eq 0}">
										<input type="radio" name="roomTypeLocation" value="<c:out value="${prices.roomType}-${hotel.key}" />" />
									</c:if>
									<c:if test="${prices.discountPrice ne 0}">
										<input type="radio" name="roomTypeLocation" value="<c:out value="${prices.roomType}-${hotel.key}" />" disabled/>
									</c:if>
									</td>
								</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:forEach>
			<input type="submit" name="action" value="Set Price" />
		</form>
	</div>
</div>
</body>
</html>