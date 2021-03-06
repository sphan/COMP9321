<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*, java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Checkout Confirmation</title>
</head>
<body>
<%@ include file="Header.html"%>
<center>
<form action="control" method="POST">
	<div>
	<table border="1" cellpadding="5">
		<tr>
			<td>Title</td>
			<td>Artist</td>
			<td>Type</td>
			<td>Publisher</td>
			<td>Price</td>
		</tr>
		<c:forEach var="item" items="${cart.items}">
			<tr>
				<td><c:out value="${item.title}" /></td>
				<td><c:out value="${item.artist}" /></td>
				<td><c:out value="${item.type}" /></td>
				<td><c:out value="${item.publisher}" /></td>
				<td><fmt:formatNumber value="${item.price}" type="currency" /></td>
			</tr>
		</c:forEach>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td>Total</td>
			<td><fmt:formatNumber value="${totalCost}" type="currency" /></td>
		</tr>
	</table>
	</div>
</form>
</center>
<center>
<form action="control" method="POST">
	<input type="submit" value="Buy" />
	<input type="hidden" name="action" value="buy" />
</form>
<form action="control" method="POST">
	<input type="submit" value="Don't want it anymore" />
	<input type="hidden" name="action" value="leave" />
</form>
</center>
</body>
</html>